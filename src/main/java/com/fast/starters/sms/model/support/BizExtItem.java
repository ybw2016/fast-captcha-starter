package com.fast.starters.sms.model.support;

import com.fast.starters.sms.enums.LifecycleType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 获取验证码注解的属性.
 *
 * @author bowen.yan
 * @since 2019-12-23
 */
@NoArgsConstructor(staticName = "of")
@Accessors(fluent = true)
@Data
public class BizExtItem {
    /**
     * 是否锁住业务.
     */
    boolean lock;

    /**
     * 锁住业务多长时间.
     */
    int lockSeconds;

    /**
     * 验证码生命周期类型.
     */
    LifecycleType lifecycleType;
}
