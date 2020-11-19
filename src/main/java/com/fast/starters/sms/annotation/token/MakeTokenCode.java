package com.fast.starters.sms.annotation.token;

import com.fast.starters.sms.constant.RedisTime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生成token注解.
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MakeTokenCode {
    /**
     * 验证类型的枚举类.
     */
    Class<? extends Enum> bizEnum();

    /**
     * 生成token验证码的唯一业务key（业务场景）.
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
