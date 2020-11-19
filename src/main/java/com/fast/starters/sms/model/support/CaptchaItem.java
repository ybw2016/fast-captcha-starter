package com.fast.starters.sms.model.support;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 图片验证码中间类.
 *
 * @author bowen.yan
 * @since 2019-11-03
 */
@AllArgsConstructor
@Data
public class CaptchaItem {
    private String captchaText;//图片验证码

    private String base64Text;//图片base64流
}
