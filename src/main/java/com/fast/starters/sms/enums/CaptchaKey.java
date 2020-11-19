package com.fast.starters.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * h5 <-> 服务端交互约定的请求入参key.
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
@Getter
@AllArgsConstructor
public enum CaptchaKey {
    SMS_CODE("smsCode", "短信验证码"),
    TOKEN("token", "token流水号"),
    IMAGE_CAPTCHA("captchaText", "图片验证码");

    private String code;
    private String desc;
}
