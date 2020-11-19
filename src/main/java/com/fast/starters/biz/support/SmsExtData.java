package com.fast.starters.biz.support;

import lombok.Data;

/**
 * @author bw
 * @since 2020-11-18
 */
@Data
public class SmsExtData {
    private String userId;
    private String tradeAmount;//交易金额
    private String accountNo;//账号
}
