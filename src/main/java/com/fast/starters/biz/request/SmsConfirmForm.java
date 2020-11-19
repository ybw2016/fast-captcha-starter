package com.fast.starters.biz.request;

import com.fast.starters.base.def.rest.ReqForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 开户请求参数-输入验证码.
 *
 * @author bowen.yan
 * @since 2019/8/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SmsConfirmForm extends ReqForm {
    private String smsCode;//短信验证码
}
