package com.fast.starters.biz.sms;

import com.fast.starters.biz.support.SmsExtData;
import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.service.AbstractSmsCaptchaService;
import com.fast.starters.sms.support.CaptchaConfigDef;
import com.fast.starters.sms.utils.AspectHelper;
import com.fast.starters.sms.utils.SmsHelper;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;

/**
 * @author bw
 * @since 2020-11-18
 */
@Service
public class SmsSendService extends AbstractSmsCaptchaService {
    @Override
    public String getBizKey(JoinPoint joinPoint, CaptchaConfigDef captchaConfigDef) {
        String uniqueKey = (String) AspectHelper.getParamValueByJoinPoint(joinPoint, "uniqueKey");
        SmsHelper.checkParam(uniqueKey, "唯一业务类型不能为空");

        return SmsHelper.toBizKey(uniqueKey, captchaConfigDef.getBizName());
    }

    @Override
    protected Msg.MsgInfoRet doSendSms(Msg.MsgInfo msgInfo) {
        Msg.MsgInfo redisMsg = smsCacheService.getMsgInfoFromRedis(msgInfo);
        SmsExtData redisMsgExt = redisMsg.getExtData(SmsExtData.class);

        String token = "短信服务生成的token";
        String effectTime = "短信服务生成的有效时间";

        return Msg.MsgInfoRet.builder()
            .token(token)
            .effectTime(effectTime)
            .build();
    }

    @Override
    protected void doCheckSms(Msg.MsgInfo msgInfo) {
        Msg.MsgInfo redisMsg = smsCacheService.getMsgInfoFromRedis(msgInfo);

        String smsCode = msgInfo.getSmsCode();
        String token = msgInfo.getToken();

        // 验证smsCode和token
    }
}
