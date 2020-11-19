package com.fast.starters.sms.service.support;

import com.fast.starters.sms.service.AbstractImageCaptchaService;
import com.fast.starters.sms.support.CaptchaConfigDef;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.aspectj.lang.JoinPoint;

/**
 * 图片验证码处理类.
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
@Slf4j
public class ImageCaptchaService extends AbstractImageCaptchaService {
    @Override
    public String getBizKey(JoinPoint joinPoint, CaptchaConfigDef captchaConfigDef) {
        throw new NotImplementedException("只有注解模式才能使用此方法");
    }
}
