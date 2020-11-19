package com.fast.starters.sms.aop.token;

import com.fast.starters.sms.annotation.token.CheckTokenCode;
import com.fast.starters.sms.aop.CheckCaptchaAspectBase;
import com.fast.starters.sms.model.support.BizExtItem;
import com.fast.starters.sms.model.support.ParamKeyValidateItem;
import com.fast.starters.sms.service.support.CaptchaService;
import com.fast.starters.sms.enums.CaptchaKey;
import com.fast.starters.sms.model.support.AspectCaptchaBizData;
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
 * 生成token切面类.
 * 通过切用户自定义注解，从而实现生成token功能
 *
 * @author bowen.yan
 * @since 2019-09-16
 */
@Slf4j
@Aspect
public class CheckTokenAspect extends CheckCaptchaAspectBase {
    @Pointcut("@annotation(com.fast.starters.sms.annotation.token.CheckTokenCode)")
    public void checkTokenCodePointcut() {
    }

    /**
     * 校验token前置处理.
     */
    @Around("checkTokenCodePointcut()")
    protected Object checkTokenCodeBeforeBiz(ProceedingJoinPoint joinPoint) {
        return checkCaptchaBeforeBiz(joinPoint);
    }

    @Override
    protected BizExtItem getBizExtItem(ProceedingJoinPoint joinPoint, CaptchaService captchaService,
                                       AspectCaptchaBizData aspectBizData) {
        // 验证通过后，执行具体的业务
        CheckTokenCode checkTokenCode = (CheckTokenCode) aspectBizData.captchaAnnotation();
        return BizExtItem.of()
            .lock(checkTokenCode.lock())
            .lockSeconds(checkTokenCode.lockSeconds())
            .lifecycleType(checkTokenCode.lifecycleType());
    }

    @Override
    protected List<ParamKeyValidateItem> getParamValueValidators() {
        return Arrays.asList(
            new ParamKeyValidateItem(CaptchaKey.TOKEN, "token验证码不能为空",
                (tokenObj, msgInfo) -> msgInfo.setToken((String) tokenObj))
        );
    }

    @Override
    protected AspectCaptchaBizData getAspectBizData(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckTokenCode checkToken = signature.getMethod().getAnnotation(CheckTokenCode.class);
        CaptchaConfigDef configDef = getCaptchaConfigDef(checkToken.bizEnum(), checkToken.bizEnumName());
        CaptchaService captchaService = getCaptchaService(configDef);

        return AspectCaptchaBizData.of()
            .captchaAnnotation(checkToken)
            .captchaConfigDef(configDef)
            .captchaService(captchaService);
    }
}