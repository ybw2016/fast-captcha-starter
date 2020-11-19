package com.fast.starters.biz.enums;


import com.fast.starters.biz.constant.SmsConfig;
import com.fast.starters.sms.model.config.SmsCaptchaConfig;
import com.fast.starters.sms.support.CaptchaConfigDef;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 信息验证服务枚举.
 *
 * @author lm
 * @since 2019-09-03
 */
@Getter
@AllArgsConstructor
public enum SmsType implements CaptchaConfigDef {
    OPEN_ACCOUNT(SmsConfig.OPEN_ACCOUNT),
    PURCHASE(SmsConfig.PURCHASE),
    RECHARGE(SmsConfig.RECHARGE),
    WITHDRAW(SmsConfig.WITHDRAW);

    private SmsCaptchaConfig captchaConfig;

    @Override
    public String getBizName() {
        return this.name();
    }
}
