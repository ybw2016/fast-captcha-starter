package com.fast.starters.sms.model.support;

import com.fast.starters.sms.service.support.CaptchaService;
import com.fast.starters.sms.support.CaptchaConfigDef;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 解析完的切面业务数据.
 *
 * @author bowen.yan
 * @since 2019-11-25
 */
@NoArgsConstructor(staticName = "of")
@Accessors(fluent = true)
@Data
public class AspectCaptchaBizData {
    private Object captchaAnnotation;
    private CaptchaConfigDef captchaConfigDef;
    private CaptchaService captchaService;
}
