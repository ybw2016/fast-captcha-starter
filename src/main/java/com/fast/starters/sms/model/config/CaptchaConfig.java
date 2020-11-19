package com.fast.starters.sms.model.config;

import com.fast.starters.sms.service.support.CaptchaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信、图片、token验证码注解模式配置类.
 *
 * @author bowen.yan
 * @since 2019-11-03
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaptchaConfig {
    /**
     * 验证码处理业务类.
     * {@link CaptchaService}
     */
    private String captchaService;

    /**
     * 业务类型描述（充值、提现、购买等）.
     */
    private String bizDesc;

    /**
     * 验证码有效时间.
     */
    private long expireTime;
}
