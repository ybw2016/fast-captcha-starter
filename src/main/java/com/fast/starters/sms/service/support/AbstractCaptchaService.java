package com.fast.starters.sms.service.support;

import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.support.CaptchaConfigDef;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 验证码处理基类.
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
@Slf4j
public abstract class AbstractCaptchaService implements CaptchaService, CacheCleaner {
    @Autowired
    protected SmsCacheService smsCacheService;

    /**
     * 构建验证码msg对象.
     *
     * @param joinPoint        当前会话上下文切面
     * @param captchaConfigDef 验证码配置
     * @return 短信实体
     */
    public Msg.MsgInfo buildMsgInfo(JoinPoint joinPoint, CaptchaConfigDef captchaConfigDef) {
        return Msg.MsgInfo.builder()
            .bizKey(getBizKey(joinPoint, captchaConfigDef))
            .captchaConfig(captchaConfigDef.getCaptchaConfig())
            .build();
    }
}