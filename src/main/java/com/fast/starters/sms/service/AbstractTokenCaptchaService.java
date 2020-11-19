package com.fast.starters.sms.service;

import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.exception.CaptchaException;
import com.fast.starters.sms.service.support.AbstractCaptchaService;
import com.fast.starters.sms.support.cache.CacheTool;
import com.fast.starters.sms.enums.MsgError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Token验证码处理类.
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
@Slf4j
public abstract class AbstractTokenCaptchaService extends AbstractCaptchaService {
    @Autowired
    private CacheTool cacheTool;

    public boolean isTokenAppendToHeader() {
        return true;
    }

    @Override
    public Object makeCaptcha(Msg.MsgInfo msgInfo) {
        String token = UUID.randomUUID().toString().replace("-", "");
        smsCacheService.saveToken(msgInfo, token);

        return token;
    }

    @Override
    public void checkCaptcha(Msg.MsgInfo inputMsgInfo) {
        String redisToken = smsCacheService.getToken(inputMsgInfo);
        if (!inputMsgInfo.getToken().equalsIgnoreCase(redisToken)) {
            throw new CaptchaException(MsgError.FAIL_CHECK_TOKEN_CODE);
        }
    }

    @Override
    public void clearSendTimesCache(Msg.MsgInfo msgInfo) {
        cacheTool.delete(smsCacheService.getTokenKey(msgInfo));
    }
}
