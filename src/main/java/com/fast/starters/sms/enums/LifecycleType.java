package com.fast.starters.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码缓存清除机制类型.
 *
 * @author bowen.yan
 * @since 2020-06-01
 */
@Getter
@AllArgsConstructor
public enum LifecycleType {
    CLEAR_AFTER_VERIFY_OK("验证码验证完成后清除"),
    CLEAR_AFTER_BIZ_SUCCESS("验证码验证完成且业务成功执行后清除");

    private String desc;
}
