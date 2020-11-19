package com.fast.starters.sms.service.support;

import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.support.cache.CacheTool;
import com.fast.starters.sms.constant.RedisTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * @author bowen.yan
 * @since 2019-09-19
 */
@Slf4j
public class SmsCacheService {
    @Autowired
    private CacheTool cacheTool;

    /**
     * 发送短信前，将相关业务数据保存到redis，以便后续验证短信验证码时使用.
     *
     * @param msgInfo 业务数据
     */
    public void saveMsgInfoToRedis(Msg.MsgInfo msgInfo) {
        String redisKey = getMsgInfoKey(msgInfo);
        log.info("保存短信业务数据到redis -> redisKey:{}, msgInfo:{}", redisKey, msgInfo);
        cacheTool.setValue(redisKey, msgInfo, msgInfo.getCaptchaConfig().getExpireTime());
    }

    /**
     * 获取缓存的业务数据，以便短信验证码验证通过后执行业务逻辑.
     *
     * @param msgInfo 业务数据
     * @return data
     */
    public Msg.MsgInfo getMsgInfoFromRedis(Msg.MsgInfo msgInfo) {
        String redisKey = getMsgInfoKey(msgInfo);
        Msg.MsgInfo redisMsgInfo = cacheTool.getValue(redisKey, Msg.MsgInfo.class);
        log.info("从redis中获取短信业务数据 -> redisKey:{}, redisMsgInfo:{}", redisKey, redisMsgInfo);
        return redisMsgInfo;
    }

    /**
     * 交易完成后清除缓存.
     */
    public void clearAllCache(Msg.MsgInfo msgInfo) {
        // 清除总数据（各种场景）
        cacheTool.delete(getMsgInfoKey(msgInfo));

        // 清除SMS数据（SMS模式专用）
        cacheTool.delete(getSmsCodeKey(msgInfo));
        cacheTool.delete(getSmsTokenKey(msgInfo));
        cacheTool.delete(getSmsSendNumKey(msgInfo));
        cacheTool.delete(getSmsSendTimeKey(msgInfo));
        cacheTool.delete(getSmsMiscDataKey(msgInfo));

        // 清除图片相关数据（token模式专用）
        cacheTool.delete(getImageTokenKey(msgInfo));

        // 清除token相关数据（image模式专用）
        cacheTool.delete(getTokenKey(msgInfo));

        log.info("从redis中清除业务数据完毕！-> bizKey:{}", msgInfo.getBizKey());
    }

    private String getRedisKey(Msg.MsgInfo msg, BizKey biz) {
        return String.format("%s_%s", msg.getBizKey(), biz.getCode());
    }

    private String getMsgInfoKey(Msg.MsgInfo msgInfo) {
        return getRedisKey(msgInfo, BizKey.MSG_INFO);
    }

    public String getSmsCodeKey(Msg.MsgInfo msgInfo) {
        return getRedisKey(msgInfo, BizKey.SMS_CODE);
    }

    public String getSmsCodeFromRedis(Msg.MsgInfo msgInfo) {
        return cacheTool.getValue(getSmsCodeKey(msgInfo));
    }

    public String getSmsTokenKey(Msg.MsgInfo msgInfo) {
        return getRedisKey(msgInfo, BizKey.SMS_TOKEN);
    }

    public String getTokenFromRedis(Msg.MsgInfo msgInfo) {
        return cacheTool.getValue(getSmsTokenKey(msgInfo));
    }

    public String getSmsSendNumKey(Msg.MsgInfo msgInfo) {
        return getRedisKey(msgInfo, BizKey.SMS_SEND_NUM);
    }

    public Integer getSendNumFromRedis(Msg.MsgInfo msgInfo) {
        return cacheTool.getInteger(getSmsSendNumKey(msgInfo));
    }

    public String getImageTokenKey(Msg.MsgInfo msgInfo) {
        return getRedisKey(msgInfo, BizKey.IMG_TOKEN);
    }

    public void saveImageCaptcha(Msg.MsgInfo msgInfo, String captchaText) {
        cacheTool.setValue(getImageTokenKey(msgInfo), captchaText, getExpire(msgInfo));
    }

    /**
     * image: 用户图片验证码信息captchaText.
     */
    public String getImageFromRedis(Msg.MsgInfo msgInfo) {
        return cacheTool.getValue(getImageTokenKey(msgInfo));
    }

    public String getSmsSendTimeKey(Msg.MsgInfo msgInfo) {
        return getRedisKey(msgInfo, BizKey.SMS_SEND_TIME);
    }

    public void saveSmsSendTime(Msg.MsgInfo msgInfo) {
        cacheTool.setValue(getSmsSendTimeKey(msgInfo), System.currentTimeMillis(),
            msgInfo.getCaptchaConfig().getExpireTime());
    }

    /**
     * 获取短信发送时间.
     */
    public Long getSmsSendTime(Msg.MsgInfo msgInfo) {
        String sendTimeKey = getSmsSendTimeKey(msgInfo);
        if (!cacheTool.hasKey(sendTimeKey)) {
            return null;
        }

        return Long.parseLong(cacheTool.getValue(sendTimeKey));
    }

    public String getTokenKey(Msg.MsgInfo msgInfo) {
        return getRedisKey(msgInfo, BizKey.TOKEN);
    }

    public void saveToken(Msg.MsgInfo msgInfo, String token) {
        cacheTool.setValue(getTokenKey(msgInfo), token, getExpire(msgInfo));
    }

    public String getToken(Msg.MsgInfo msgInfo) {
        return cacheTool.getValue(getTokenKey(msgInfo));
    }

    public String getSmsTotalTimesKey(Msg.MsgInfo msgInfo) {
        return getRedisKey(msgInfo, BizKey.SMS_TOTAL_TIMES);
    }

    public Integer getSmsTotalTimes(Msg.MsgInfo msgInfo) {
        return cacheTool.getInteger(getSmsTotalTimesKey(msgInfo));
    }

    public String getSmsMiscDataKey(Msg.MsgInfo msgInfo) {
        return getRedisKey(msgInfo, BizKey.SMS_MISC_DATA);
    }

    public <T> T getSmsMiscData(Msg.MsgInfo msgInfo, Class<T> clazz) {
        return cacheTool.getValue(getSmsMiscDataKey(msgInfo), clazz);
    }

    /**
     * 发送短信缓存key枚举.
     *
     * @author bowen.yan
     * @since 2019-09-20
     */
    @AllArgsConstructor
    @Getter
    private enum BizKey {
        MSG_INFO("msgInfo", "发送短信业务数据"),
        SMS_CODE("smsCode", "收到的短信验证码"),
        SMS_TOKEN("smsToken", "发送短信流水号"),
        SMS_SEND_NUM("smsSendNum", "短信发送次数"),
        SMS_SEND_TIME("smsSendTime", "短信发送时间戳"),
        SMS_TOTAL_TIMES("smsTotalTimes", "一天内总使用次数"),
        SMS_MISC_DATA("smsMiscData", "发送短信时的扩展数据"),
        IMG_TOKEN("imgToken", "生成图片验证码流水号"),
        TOKEN("token", "token唯一流水号");

        private String code;
        private String desc;
    }

    private long getExpire(Msg.MsgInfo msgInfo) {
        return Objects.nonNull(msgInfo.getCaptchaConfig())
            ? msgInfo.getCaptchaConfig().getExpireTime() : RedisTime.MINUTE * 5;
    }
}
