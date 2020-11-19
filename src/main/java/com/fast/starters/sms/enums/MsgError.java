package com.fast.starters.sms.enums;

import com.fast.starters.base.error.ErrorDef;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码-验证码相关（短信、图片验证码等）（0003****）.
 *
 * @author bowen.yan
 */
@Getter
@AllArgsConstructor
public enum MsgError implements ErrorDef {
    FAIL_CHECK_SMS_CODE("00030001", "短信识别码不存在"),
    FAIL_CHECK_IMG_CODE("00030002", "图片验证码错误"),
    FAIL_CHECK_TOKEN_CODE("00030003", "token流水号验证码错误"),
    SEND_SMS_REQUEST_TOO_FREQUENCY("00030004", "发送短信请求过于频繁"),
    EXCEED_MAX_TOTAL_TIMES("00030005", "超过当天允许最大次数");

    private String errCode;
    private String errMsg;
}
