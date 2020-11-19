package com.fast.starters.base.exception;

import com.fast.starters.base.error.ErrorDef;
import lombok.Data;

/**
 * @author bw
 * @since 2020-11-18
 */
@Data
public class BaseException extends RuntimeException {

    private String code;
    private String msg;
    private Object data;

    public BaseException(ErrorDef errorDef) {
        super(errorDef.getErrMsg());
        this.setCode(errorDef.getErrCode());
        this.setCode(errorDef.getErrMsg());
    }

    public BaseException(String code, String msg) {
        super(msg);
        this.setCode(code);
        this.setMsg(msg);
    }

    public BaseException(String msg) {
        super(msg);
        this.setMsg(msg);
    }

    public void setCode(String code) {
        this.code = code;
    }
}

