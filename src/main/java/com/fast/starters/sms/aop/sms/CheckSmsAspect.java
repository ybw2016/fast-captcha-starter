package com.fast.starters.sms.aop.sms;

import com.fast.starters.sms.annotation.sms.CheckSmsCode;
import com.fast.starters.sms.aop.CheckCaptchaAspectBase;
import com.fast.starters.sms.enums.CaptchaKey;
import com.fast.starters.sms.model.support.AspectCaptchaBizData;
import com.fast.starters.sms.model.support.BizExtItem;
import com.fast.starters.sms.model.support.ParamKeyValidateItem;
import com.fast.starters.sms.service.support.AbstractCaptchaService;
import com.fast.starters.sms.service.support.CaptchaService;
import com.fast.starters.sms.support.CaptchaConfigDef;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.Arrays;
import java.util.List;

/**
 * 发送信息切面类.
 * 通过切用户自定义注解，从而实现发短信功能
 *
 * @author bowen.yan
 * @since 2019-09-16
 */
@Slf4j
@Aspect
public class CheckSmsAspect extends CheckCaptchaAspectBase {
    @Pointcut("@annotation(com.fast.starters.sms.annotation.sms.CheckSmsCode)")
    public void checkSmsCodePointcut() {
    }

    /**
     * 校验短信前置处理.
     */
    @Around("checkSmsCodePointcut()")
    protected Object checkSmsCodeBeforeBiz(ProceedingJoinPoint joinPoint) {
        return checkCaptchaBeforeBiz(joinPoint);
    }

    @Override
    protected BizExtItem getBizExtItem(ProceedingJoinPoint joinPoint, CaptchaService captchaService,
                                       AspectCaptchaBizData aspectBizData) {
        // 验证通过后，执行具体的业务
        CheckSmsCode checkSmsCode = (CheckSmsCode) aspectBizData.captchaAnnotation();
        return BizExtItem.of()
            .lock(checkSmsCode.lock())
            .lockSeconds(checkSmsCode.lockSeconds())
            .lifecycleType(checkSmsCode.lifecycleType());
    }

    @Override
    protected List<ParamKeyValidateItem> getParamValueValidators() {
        return Arrays.asList(
            new ParamKeyValidateItem(CaptchaKey.SMS_CODE, "短信验证码不能为空",
                (smsCodeObj, msgInfo) -> msgInfo.setSmsCode((String) smsCodeObj))
        );
    }

    @Override
    protected AspectCaptchaBizData getAspectBizData(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckSmsCode checkSmsCode = signature.getMethod().getAnnotation(CheckSmsCode.class);
        CaptchaConfigDef config = getCaptchaConfigDef(checkSmsCode.bizEnum(), checkSmsCode.bizEnumName());
        AbstractCaptchaService captchaService = getCaptchaService(config);

        return AspectCaptchaBizData.of()
            .captchaAnnotation(checkSmsCode)
            .captchaConfigDef(config)
            .captchaService(captchaService);
    }
}