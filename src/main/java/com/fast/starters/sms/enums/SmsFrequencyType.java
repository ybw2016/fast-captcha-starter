package com.fast.starters.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请求过快处理策略.
 *
 * @author bowen.yan
 * @since 2020-06-01
 */
@Getter
@AllArgsConstructor
public enum SmsFrequencyType {
    NO_ACTION("不处理"),
    SWITCH_IMAGE_CAPTCHA("切换图片验证码"),
    THROW_EXCEPTION("抛出异常");

    private String desc;
}
