package com.fast.starters.sms.model.support;

import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.enums.CaptchaKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.BiConsumer;

/**
 * 传入参数验证器.
 *
 * @author bowen.yan
 * @since 2019-12-02
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParamKeyValidateItem {
    private CaptchaKey captchaKey;
    private String errorMsg;
    private BiConsumer<Object, Msg.MsgInfo> paramValueBinder;
}
