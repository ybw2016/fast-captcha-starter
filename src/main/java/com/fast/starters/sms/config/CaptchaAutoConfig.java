package com.fast.starters.sms.config;

import com.fast.starters.sms.aop.image.CheckImageAspect;
import com.fast.starters.sms.aop.image.MakeImageAspect;
import com.fast.starters.sms.aop.sms.CheckSmsAspect;
import com.fast.starters.sms.aop.sms.SendSmsAspect;
import com.fast.starters.sms.aop.token.CheckTokenAspect;
import com.fast.starters.sms.aop.token.MakeTokenAspect;
import com.fast.starters.sms.service.AbstractImageCaptchaService;
import com.fast.starters.sms.service.AbstractSmsCaptchaService;
import com.fast.starters.sms.service.AbstractTokenCaptchaService;
import com.fast.starters.sms.service.support.ImageCaptchaService;
import com.fast.starters.sms.service.support.SmsCacheService;
import com.fast.starters.sms.support.cache.CacheTool;
import com.fast.starters.sms.support.cache.LockTool;
import com.fast.starters.sms.support.cache.impl.RedisCacheTool;
import com.fast.starters.sms.support.cache.impl.RedisLockTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码生成、发送自动装配类（支持类、切面类）.
 *
 * @author bowen.yan
 * @since 2019-11-05
 */
@Configuration
//@AutoConfigureAfter(RedisAutoConfig.class)
@ConditionalOnProperty(name = "fast.starters.sms.enabled", havingValue = "true", matchIfMissing = true)
public class CaptchaAutoConfig {
    @Bean
    public SmsCacheService smsCacheService() {
        return new SmsCacheService();
    }


    // 短信机制实例化
    @ConditionalOnBean(AbstractSmsCaptchaService.class)
    @Bean
    public SendSmsAspect sendSmsAspect() {
        return new SendSmsAspect();
    }

    @ConditionalOnBean(AbstractSmsCaptchaService.class)
    @Bean
    public CheckSmsAspect checkSmsAspect() {
        return new CheckSmsAspect();
    }


    // token机制实例化
    @ConditionalOnBean(AbstractTokenCaptchaService.class)
    @Bean
    public MakeTokenAspect makeTokenAspect() {
        return new MakeTokenAspect();
    }

    @ConditionalOnBean(AbstractTokenCaptchaService.class)
    @Bean
    public CheckTokenAspect checkTokenAspect() {
        return new CheckTokenAspect();
    }


    /**
     * 图片机制实例化.
     * 图片验证码支持两种模式：注解模式、手动模式
     * 当项目选择手动模式（手动生成、验证图片验证码时，需要一个默认的bean）
     */
    @ConditionalOnMissingBean(AbstractImageCaptchaService.class)
    @Bean
    public ImageCaptchaService imageCaptchaService() {
        return new ImageCaptchaService();
    }

    @ConditionalOnBean(AbstractImageCaptchaService.class)
    @ConditionalOnMissingBean(ImageCaptchaService.class)
    @Bean
    public MakeImageAspect makeImageAspect() {
        return new MakeImageAspect();
    }

    @ConditionalOnBean(AbstractImageCaptchaService.class)
    @ConditionalOnMissingBean(ImageCaptchaService.class)
    @Bean
    public CheckImageAspect checkImageAspect() {
        return new CheckImageAspect();
    }


    // 初始化缓存工具类
    @ConditionalOnMissingBean(CacheTool.class)
    @Bean
    public CacheTool cacheTool() {
        return new RedisCacheTool();
    }

    // 初始化分布式锁工具类
    @ConditionalOnMissingBean(LockTool.class)
    @Bean
    public LockTool lockTool() {
        return new RedisLockTool();
    }
}
