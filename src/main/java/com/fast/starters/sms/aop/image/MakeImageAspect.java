package com.fast.starters.sms.aop.image;

import com.fast.starters.sms.annotation.image.MakeImageCode;
import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.exception.CaptchaException;
import com.fast.starters.sms.model.support.CaptchaItem;
import com.fast.starters.sms.service.support.CaptchaService;
import com.fast.starters.sms.vo.MakeImageCodeVo;
import com.fast.starters.sms.vo.setter.ImageCaptchaSettable;
import com.fast.starters.sms.vo.setter.TokenCaptchaSettable;
import com.fast.starters.sms.aop.MakeCaptchaAspectBase;
import com.fast.starters.sms.model.support.AspectCaptchaBizData;
import com.fast.starters.sms.support.CaptchaConfigDef;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 生成image切面类.
 * 通过切用户自定义注解，从而实现生成token功能
 *
 * @author bowen.yan
 * @since 2019-09-16
 */
@Slf4j
@Aspect
public class MakeImageAspect extends MakeCaptchaAspectBase {
    @Pointcut("@annotation(com.fast.starters.sms.annotation.image.MakeImageCode)")
    public void makeImageCodePointcut() {
    }

    /**
     * 生成image前的业务逻辑拦截.
     * 防重复提交支持
     */
    @Around("makeImageCodePointcut()")
    public Object lockAndExecuteBizBeforeMakeImageCode(ProceedingJoinPoint joinPoint) {
        AspectCaptchaBizData aspectBizData = getAspectBizData(joinPoint);
        MakeImageCode makeImageCode = (MakeImageCode) aspectBizData.captchaAnnotation();
        String bizKey = aspectBizData.captchaService().getBizKey(joinPoint, aspectBizData.captchaConfigDef());

        return lockAndExecuteBiz(joinPoint, aspectBizData, makeImageCode.lock(), bizKey, makeImageCode.lockSeconds());
    }

    @Override
    protected void bindCaptchaToVo(Object captchaItem, Object retVal,
                                   CaptchaService captchaService, Msg.MsgInfo msgInfo) {
        CaptchaItem imageCaptchaItem = (CaptchaItem) captchaItem;

        if (retVal instanceof MakeImageCodeVo) {
            ((MakeImageCodeVo) retVal).setImg(imageCaptchaItem.getBase64Text());
        } else if (retVal instanceof TokenCaptchaSettable) {
            ((ImageCaptchaSettable) retVal).setImageBase64Text(imageCaptchaItem.getBase64Text());
        } else {
            throw new CaptchaException("接收图片验证码的Vo类型不正确");
        }
    }

    @Override
    protected AspectCaptchaBizData getAspectBizData(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        MakeImageCode makeImage = signature.getMethod().getAnnotation(MakeImageCode.class);
        CaptchaConfigDef configDef = getCaptchaConfigDef(makeImage.bizEnum(), makeImage.bizEnumName());
        CaptchaService captchaService = getCaptchaService(configDef);

        return AspectCaptchaBizData.of()
            .captchaAnnotation(makeImage)
            .captchaConfigDef(configDef)
            .captchaService(captchaService);
    }
}
