package com.fast.starters.sms.support;

import com.fast.starters.sms.model.config.CaptchaConfig;

/**
 * 验证码发送配置.
 *
 * @author bowen.yan
 * @since 2019-11-05
 */
public interface CaptchaConfigDef {
    /**
     * 业务场景名称（如充值、转账、提现等）.
     */
    String getBizName();

    /**
     * 验证码配置.
     */
    CaptchaConfig getCaptchaConfig();
}