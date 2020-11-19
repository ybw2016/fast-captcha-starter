package com.fast.starters.sms.aop.image;

import com.fast.starters.sms.annotation.image.CheckImageCode;
import com.fast.starters.sms.aop.CheckCaptchaAspectBase;
import com.fast.starters.sms.enums.CaptchaKey;
import com.fast.starters.sms.model.support.AspectCaptchaBizData;
import com.fast.starters.sms.model.support.BizExtItem;
import com.fast.starters.sms.model.support.ParamKeyValidateItem;
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
 * 生成图片验证码切面类.
 * 通过切用户自定义注解，从而实现生成token功能
 *
 * @author bowen.yan
 * @since 2019-09-16
 */
@Slf4j
@Aspect
public class CheckImageAspect extends CheckCaptchaAspectBase {
    @Pointcut("@annotation(com.fast.starters.sms.annotation.image.CheckImageCode)")
    public void checkImageCodePointcut() {
    }

    /**
     * 校验图片验证码前置处理.
     */
    @Around("checkImageCodePointcut()")
    protected Object checkTokenCodeBeforeBiz(ProceedingJoinPoint joinPoint) {
        return checkCaptchaBeforeBiz(joinPoint);
    }

    @Override
    protected BizExtItem getBizExtItem(ProceedingJoinPoint joinPoint, CaptchaService captchaService,
                                       AspectCaptchaBizData aspectBizData) {
        // 验证通过后，执行具体的业务
        CheckImageCode checkImageCode = (CheckImageCode) aspectBizData.captchaAnnotation();
        return BizExtItem.of()
            .lock(checkImageCode.lock())
            .lockSeconds(checkImageCode.lockSeconds())
            .lifecycleType(checkImageCode.lifecycleType());
    }

    @Override
    protected List<ParamKeyValidateItem> getParamValueValidators() {
        return Arrays.asList(
            new ParamKeyValidateItem(CaptchaKey.IMAGE_CAPTCHA, "图片验证码不能为空",
                (captchaTextObj, msgInfo) -> msgInfo.setCaptchaText((String) captchaTextObj))
        );
    }

    @Override
    protected AspectCaptchaBizData getAspectBizData(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckImageCode checkImage = signature.getMethod().getAnnotation(CheckImageCode.class);
        CaptchaConfigDef configDef = getCaptchaConfigDef(checkImage.bizEnum(), checkImage.bizEnumName());
        CaptchaService captchaService = getCaptchaService(configDef);

        return AspectCaptchaBizData.of()
            .captchaAnnotation(checkImage)
            .captchaConfigDef(configDef)
            .captchaService(captchaService);
    }
}