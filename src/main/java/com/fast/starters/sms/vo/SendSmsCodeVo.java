package com.fast.starters.sms.vo;

import com.fast.starters.base.def.rest.RspVo;
import com.fast.starters.sms.enums.OverLimitType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信验证码发送完成后的返回vo.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SendSmsCodeVo extends RspVo {
    /**
     * 切换图片验证标志 true:不需要  false:需要
     *
     * @see OverLimitType
     */
    boolean overLimit;
}
