package com.fast.starters.sms.annotation.image;

import com.fast.starters.sms.constant.RedisTime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 图片验证注解.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MakeImageCode {
    /**
     * 验证类型的枚举类.
     */
    Class<? extends Enum> bizEnum();

    /**
     * verifyType的名称.
     */
    String bizEnumName();

    /**
     * token过期时间.
     */
    long expire() default RedisTime.MINUTE * 5;

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
}