package com.fast.starters.sms.aop;

import com.fast.starters.base.support.SpringContextHolder;
import com.fast.starters.sms.service.support.AbstractCaptchaService;
import com.fast.starters.sms.service.support.SmsCacheService;
import com.fast.starters.sms.support.CaptchaConfigDef;
import com.fast.starters.sms.support.cache.LockTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

/**
 * 验证码切面类.
 * 通过切用户自定义注解，从而实现验证码功能
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
@Slf4j
public abstract class CaptchaAspectBase implements Ordered {
    @Autowired
    protected LockTool lockTool;
    @Autowired
    protected SmsCacheService smsCacheService;

    protected AbstractCaptchaService getCaptchaService(CaptchaConfigDef captchaConfigDef) {
        String captchaService = captchaConfigDef.getCaptchaConfig().getCaptchaService();
        return (AbstractCaptchaService) SpringContextHolder.getBean(captchaService);
    }

    protected CaptchaConfigDef getCaptchaConfigDef(Class<? extends Enum> bizEnum, String bizEnumName) {
        try {
            return (CaptchaConfigDef) Enum.valueOf(bizEnum, bizEnumName);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(
                String.format("无验证码配置实现类 -> bizEnum:%s, bizEnumName:%s", bizEnum, bizEnumName), ex);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }
}
