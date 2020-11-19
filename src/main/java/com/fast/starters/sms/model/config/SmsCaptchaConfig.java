package com.fast.starters.sms.model.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信验证码注解模式配置类.
 *
 * @author bowen.yan
 * @since 2019-11-03
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SmsCaptchaConfig extends CaptchaConfig {
    /**
     * 短信发送间隔时间（防刷机制，比如：1分钟之内最多请求3次，和overLimit配合使用）.
     * 间隔期内重复请求发送短信，只记录发送次数，不真实调用短信平台发送短信
     */
    private long interval;

    /**
     * 控制发送短信次数.
     * 短信验证码输入错误阈值，例如：短信验证码失败3次即切换图片验证码
     * overLimit = N    超过N次则切换图片验证码
     * overLimit = 0    不限制次数
     * overLimit = -N   超过N次则直接抛异常，不切换图片验证码
     */
    private int overLimit;

    /**
     * 一天内短信验证码最多使用次数.
     * <p></p>
     * totalTimes = N   超过发送次数，则该用户该业务不能再次发送短信
     * totalTimes = -1  不限制一天发送总次数
     */
    private int totalTimes;

    public SmsCaptchaConfig(String captchaService, String bizDesc, long expireTime, long interval, int overLimit,
                            int totalTimes) {
        super(captchaService, bizDesc, expireTime);
        this.interval = interval;
        this.overLimit = overLimit;
        this.totalTimes = totalTimes;
    }

    public SmsCaptchaConfig(String captchaService, String bizDesc, long expireTime, long interval, int overLimit) {
        this(captchaService, bizDesc, expireTime, interval, overLimit, -1);
    }
}
