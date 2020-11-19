package com.fast.starters.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码是否超出给定时间内次数类型.
 * 该标志将返回给APP、H5，以便告知是否需要切换图片验证码
 *
 * @author bowen.yan
 * @since 2020-06-01
 */
@Getter
@AllArgsConstructor
public enum OverLimitType {
    OVER_LIMIT(false, "超过给定时间内最大限制次数"),// 返回false给APP、H5告知端上应切换图片验证码
    NOT_OVER_LIMIT(true, "未超过给定时间内最大限制次数");

    private boolean value;
    private String desc;
}
