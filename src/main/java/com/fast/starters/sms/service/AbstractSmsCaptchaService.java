package com.fast.starters.sms.service;

import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.enums.MsgError;
import com.fast.starters.sms.enums.OverLimitType;
import com.fast.starters.sms.enums.SmsFrequencyType;
import com.fast.starters.sms.exception.CaptchaException;
import com.fast.starters.sms.model.config.SmsCaptchaConfig;
import com.fast.starters.sms.service.support.AbstractCaptchaService;
import com.fast.starters.sms.support.cache.CacheTool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * 短信发送处理基类.
 * 具体发送短信，由用户自定义
 *
 * @author bowen.yan
 * @since 2019-09-19
 */
@Slf4j
public abstract class AbstractSmsCaptchaService extends AbstractCaptchaService {
    @Autowired
    private CacheTool cacheTool;

    protected abstract Msg.MsgInfoRet doSendSms(Msg.MsgInfo msgInfo);

    protected abstract void doCheckSms(Msg.MsgInfo msgInfo);

    @Override
    public Object makeCaptcha(Msg.MsgInfo msgInfo) {
        // 1. 发送总次数、当前发送次数、防刷判断
        SendTimeCheckResult sendTimeCheckResult = checkSendTimes(msgInfo);
        if (OverLimitType.OVER_LIMIT.equals(sendTimeCheckResult.getOverLimitType())) {
            return false; // 超过发送次数（如3次），则切换图片验证码
        }
        if (sendTimeCheckResult.isSkipSendSms()) {
            return true; // 未超过发送次数、但在间隔期内，则直接返回，不重复发送短信
        }

        // 2. 本次发送短信
        Msg.MsgInfoRet msgInfoRet = doSendSms(msgInfo);
        smsCacheService.saveSmsSendTime(msgInfo);

        String sendNumKey = smsCacheService.getSmsSendNumKey(msgInfo);
        String totalTimesKey = smsCacheService.getSmsTotalTimesKey(msgInfo);
        int sendNum = sendTimeCheckResult.getSendNum();
        int totalSendTimes = sendTimeCheckResult.getTotalSendTimes();

        String effectTime = msgInfoRet.getEffectTime();
        String smsTokenKey = smsCacheService.getSmsTokenKey(msgInfo);
        cacheTool.setInteger(sendNumKey, ++sendNum, getSmsInterval(msgInfo));
        cacheTool.setInteger(totalTimesKey, ++totalSendTimes, todayEndExpireSeconds());// 总发送次数当天一直记载
        cacheTool.setValue(smsTokenKey, msgInfoRet.getToken(), Integer.valueOf(effectTime));

        // 保存短信验证码及其他数据（短信平台模式时会收到短信验证码、此种机制短信验证由自己应用来验证）
        if (StringUtils.isNotBlank(msgInfoRet.getSmsCode())) {
            String smsCodeKey = smsCacheService.getSmsCodeKey(msgInfo);
            cacheTool.setValue(smsCodeKey, msgInfoRet.getSmsCode(), Integer.valueOf(effectTime));
        }
        if (Objects.nonNull(msgInfoRet.getMiscData())) {
            String smsMiscDataKey = smsCacheService.getSmsMiscDataKey(msgInfo);
            cacheTool.setValue(smsMiscDataKey, msgInfoRet.getMiscData(), Integer.valueOf(effectTime));
        }

        log.info("设置短信验证码 -> smsTokenKey:{}, token:{}, sendNum:{}, totalSendTimes:{}, smsCode:{}, effectTime:{}",
            smsTokenKey, msgInfoRet.getToken(), sendNum, totalSendTimes, msgInfoRet.getSmsCode(), effectTime);

        return true;
    }

    @Override
    public void checkCaptcha(Msg.MsgInfo msgInfo) {
        // 防止重复提交验证短信接口
        String token = smsCacheService.getTokenFromRedis(msgInfo);
        if (StringUtils.isEmpty(token)) {
            throw new CaptchaException(MsgError.FAIL_CHECK_SMS_CODE);
        }

        doCheckSms(msgInfo);

        String tokenKey = smsCacheService.getSmsTokenKey(msgInfo);
        log.info("校验短信验证码校验成功, tokenKey:{}", tokenKey);
    }

    @Override
    public void clearSendTimesCache(Msg.MsgInfo msgInfo) {
        cacheTool.delete(smsCacheService.getSmsSendNumKey(msgInfo));
        cacheTool.delete(smsCacheService.getSmsSendTimeKey(msgInfo));
        cacheTool.delete(smsCacheService.getSmsTokenKey(msgInfo));
        cacheTool.delete(smsCacheService.getSmsCodeKey(msgInfo));
        cacheTool.delete(smsCacheService.getSmsMiscDataKey(msgInfo));
    }

    private SendTimeCheckResult checkSendTimes(Msg.MsgInfo msgInfo) {
        SendTimeCheckResult sendTimeCheckResult = new SendTimeCheckResult();

        // 1. 判断总的发送次数(一天之内)
        int allowTotalTimes = ((SmsCaptchaConfig) msgInfo.getCaptchaConfig()).getTotalTimes();
        if (allowTotalTimes != -1) {
            Integer totalTimes = smsCacheService.getSmsTotalTimes(msgInfo);
            if (totalTimes == null) {
                totalTimes = 0;
            } else {
                if (totalTimes >= allowTotalTimes) {
                    throw new CaptchaException(MsgError.EXCEED_MAX_TOTAL_TIMES);
                }
            }
            sendTimeCheckResult.setTotalSendTimes(totalTimes);
        }

        String sendNumKey = smsCacheService.getSmsSendNumKey(msgInfo);
        Integer sendNum = smsCacheService.getSendNumFromRedis(msgInfo);
        if (Objects.isNull(sendNum)) {
            sendNum = 0;
        }

        // 2. 防刷机制：发送间隔检查
        // 2.1 发送短信间隔（比如：20秒前发过一次，则现在不允许重复发送）
        Long smsSendTime = smsCacheService.getSmsSendTime(msgInfo);
        long interval = getSmsInterval(msgInfo);
        boolean checkSmsInSendInterval = isSmsInSendInterval(smsSendTime, interval);
        if (checkSmsInSendInterval) {
            log.info("{}秒内同一种业务请求记录次数 -> sendNumKey:{}", interval, sendNumKey);
            cacheTool.setInteger(sendNumKey, ++sendNum, interval);
            sendTimeCheckResult.setSendNum(sendNum);
        }
        log.info("短信验证码sendNumKey:{}, 已发送次数:{}", sendNumKey, sendNum);

        OverLimitStrategy strategy = resolverOverLimit(((SmsCaptchaConfig) msgInfo.getCaptchaConfig()).getOverLimit());

        // 2.2 发送次数检查
        // 发送短信请求太快则切换图片验证码
        if (strategy.needCheck() && sendNum > strategy.getOverLimit()) {
            if (SmsFrequencyType.THROW_EXCEPTION.equals(strategy.getSmsFrequencyType())) {
                throw new CaptchaException(MsgError.SEND_SMS_REQUEST_TOO_FREQUENCY);
            }

            // 切换图片验证码时重置短信发送次数
            sendNum = 0;
            // 重置缓存中的短信发送次数
            cacheTool.setInteger(sendNumKey, sendNum, interval);

            sendTimeCheckResult.setSendNum(sendNum);
            sendTimeCheckResult.setOverLimitType(OverLimitType.OVER_LIMIT);
            sendTimeCheckResult.setSkipSendSms(true);
            return sendTimeCheckResult;
        }

        // 2.3 防刷
        if (checkSmsInSendInterval) {
            log.info("短信发送时间未超过发送时间间隔，不重新发送短信-> sendNum:{}, bizKey:{}", sendNum, msgInfo.getBizKey());
            sendTimeCheckResult.setOverLimitType(OverLimitType.NOT_OVER_LIMIT);
            sendTimeCheckResult.setSkipSendSms(true);
            return sendTimeCheckResult;
        }

        // 3 检查结果：可调用短信平台发送短信接口
        sendTimeCheckResult.setSendNum(sendNum);
        sendTimeCheckResult.setOverLimitType(OverLimitType.NOT_OVER_LIMIT);
        sendTimeCheckResult.setSkipSendSms(false);

        return sendTimeCheckResult;
    }

    private long getSmsInterval(Msg.MsgInfo msgInfo) {
        return ((SmsCaptchaConfig) msgInfo.getCaptchaConfig()).getInterval();
    }

    private boolean isSmsInSendInterval(Long smsSendTime, long intervalSeconds) {
        if (smsSendTime == null) {
            return false;
        }

        long seconds = (System.currentTimeMillis() - smsSendTime) / 1000;
        return seconds < intervalSeconds;
    }

    /**
     * 获取处理输入过快策略.
     *
     * @param overLimit 限制次数
     * @return 处理策略
     */
    private OverLimitStrategy resolverOverLimit(int overLimit) {
        if (overLimit == 0) {
            return new OverLimitStrategy(0, SmsFrequencyType.NO_ACTION);
        }
        if (overLimit < 0) {
            return new OverLimitStrategy(Math.abs(overLimit), SmsFrequencyType.THROW_EXCEPTION);
        }
        return new OverLimitStrategy(overLimit, SmsFrequencyType.SWITCH_IMAGE_CAPTCHA);
    }

    @Data
    private class SendTimeCheckResult {
        private int sendNum;
        private int totalSendTimes;
        private OverLimitType overLimitType; //是否超限
        private boolean skipSendSms; //是否跳过发送
    }

    @AllArgsConstructor
    @Data
    private class OverLimitStrategy {
        private int overLimit;
        private SmsFrequencyType smsFrequencyType;

        boolean needCheck() {
            return overLimit > 0;
        }
    }

    public static long todayEndExpireSeconds() {
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), LocalDateTime.now().with(LocalTime.MAX));
    }
}
