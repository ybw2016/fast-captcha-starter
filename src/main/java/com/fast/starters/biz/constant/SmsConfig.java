package com.fast.starters.biz.constant;

import com.fast.starters.sms.constant.RedisTime;
import com.fast.starters.sms.model.config.SmsCaptchaConfig;

/**
 * @author bw
 * @since 2020-11-18
 */
public class SmsConfig {
    public static final SmsCaptchaConfig OPEN_ACCOUNT = new SmsCaptchaConfig("smsSendService",
        "开户", RedisTime.MINUTE * 5, RedisTime.MINUTE, 5);

    public static final SmsCaptchaConfig PURCHASE = new SmsCaptchaConfig("smsSendService",
        "购买", RedisTime.MINUTE * 5, RedisTime.MINUTE, 3);

    public static final SmsCaptchaConfig RECHARGE = new SmsCaptchaConfig("smsSendService",
        "充值", RedisTime.MINUTE * 5, RedisTime.MINUTE, 3);

    public static final SmsCaptchaConfig WITHDRAW = new SmsCaptchaConfig("smsSendService",
        "提现", RedisTime.MINUTE * 5, RedisTime.MINUTE, 3);
}

