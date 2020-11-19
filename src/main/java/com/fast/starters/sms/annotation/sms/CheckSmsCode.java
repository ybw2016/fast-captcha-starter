package com.fast.starters.sms.annotation.sms;

import com.fast.starters.sms.enums.LifecycleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 短信验证注解.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CheckSmsCode {
    /**
     * 验证类型的枚举类.
     */
    Class<? extends Enum> bizEnum();

    /**
     * verifyType的名称.
     */
    String bizEnumName();

    /**
     * 是否锁住业务.
     * 说明：此功能为防重复提交
     *
     * @return boolean
     */
    boolean lock() default true;

    /**
     * 锁住业务多长时间.
     * 默认20s
     *
     * @return seconds
     */
    int lockSeconds() default 5;

    /**
     * 验证码生命周期.
     * 验证码验证通过后的处理时机：验证成功后就清除还是等业务处理成功后才能清除
     *
     * @return lifecycleType
     */
    LifecycleType lifecycleType() default LifecycleType.CLEAR_AFTER_VERIFY_OK;
}