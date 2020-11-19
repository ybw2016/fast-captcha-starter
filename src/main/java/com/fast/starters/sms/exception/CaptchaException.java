package com.fast.starters.sms.exception;

import com.fast.starters.base.error.ErrorDef;
import com.fast.starters.base.exception.BaseException;

/**
 * 短信处理异常时，抛出的各种异常.
 *
 * @author bowen.yan
 * @since 2019-11-03
 */
public class CaptchaException extends BaseException {
    public CaptchaException(ErrorDef errorDef) {
        super(errorDef);
    }

    public CaptchaException(String code, String msg) {
        super(code, msg);
    }

    public CaptchaException(String msg) {
        super(msg);
    }
}
