package com.fast.starters.sms.service;

import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.exception.CaptchaException;
import com.fast.starters.sms.model.support.CaptchaItem;
import com.fast.starters.sms.service.support.AbstractCaptchaService;
import com.fast.starters.sms.support.cache.CacheTool;
import com.fast.starters.sms.utils.CaptchaUtils;
import com.fast.starters.sms.enums.MsgError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 图片验证码处理类.
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
@Slf4j
public abstract class AbstractImageCaptchaService extends AbstractCaptchaService {
    @Autowired
    private CacheTool cacheTool;

    @Override
    public Object makeCaptcha(Msg.MsgInfo msgInfo) {
        return makeAndCacheImageCaptcha(msgInfo, makeCaptchaItem(msgInfo));
    }

    @Override
    public void checkCaptcha(Msg.MsgInfo inputMsgInfo) {
        // 从redis中找回的图片验证码
        String redisCaptchaText = smsCacheService.getImageFromRedis(inputMsgInfo);
        // 从redis中读取的msgInfo，跟传入的inputMsgInfo对比图片验证码
        if (!inputMsgInfo.getCaptchaText().equalsIgnoreCase(redisCaptchaText)) {
            throw new CaptchaException(MsgError.FAIL_CHECK_IMG_CODE);
        }
    }

    /**
     * 生成图片验证码.
     * 下游自类可重写此类，生成自定义的图片验证码
     */
    public CaptchaItem makeCaptchaItem(Msg.MsgInfo msgInfo) {
        return CaptchaUtils.generateCaptchaItem();
    }

    private Object makeAndCacheImageCaptcha(Msg.MsgInfo msgInfo, CaptchaItem captchaItem) {
        smsCacheService.saveImageCaptcha(msgInfo, captchaItem.getCaptchaText());

        return new CaptchaItem(captchaItem.getCaptchaText(),
            "data:image/jpeg;base64," + captchaItem.getBase64Text());
    }

    @Override
    public void clearSendTimesCache(Msg.MsgInfo msgInfo) {
        cacheTool.delete(smsCacheService.getImageTokenKey(msgInfo));
    }
}
