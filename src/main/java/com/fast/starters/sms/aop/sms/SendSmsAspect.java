package com.fast.starters.sms.aop.sms;

import com.fast.starters.sms.annotation.sms.SendSmsCode;
import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.exception.CaptchaException;
import com.fast.starters.sms.vo.setter.SmsCaptchaSettable;
import com.fast.starters.sms.aop.MakeCaptchaAspectBase;
import com.fast.starters.sms.model.support.AspectCaptchaBizData;
import com.fast.starters.sms.service.support.CaptchaService;
import com.fast.starters.sms.support.CaptchaConfigDef;
import com.fast.starters.sms.vo.SendSmsCodeVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 发送信息切面类.
 * 通过切用户自定义注解，从而实现发短信功能
 *
 * @author bowen.yan
 * @since 2019-09-16
 */
@Slf4j
@Aspect
public class SendSmsAspect extends MakeCaptchaAspectBase {
    @Pointcut("@annotation(com.fast.starters.sms.annotation.sms.SendSmsCode)")
    public void sendSmsCodePointcut() {
    }

    /**
     * 发送短信前的业务逻辑拦截.
     * 防重复提交支持
     */
    @Around("sendSmsCodePointcut()")
    public Object lockAndExecuteBizBeforeSendSms(ProceedingJoinPoint joinPoint) {
        AspectCaptchaBizData aspectBizData = getAspectBizData(joinPoint);
        SendSmsCode sendSmsCode = (SendSmsCode) aspectBizData.captchaAnnotation();
        String bizKey = aspectBizData.captchaService().getBizKey(joinPoint, aspectBizData.captchaConfigDef());

        return lockAndExecuteBiz(joinPoint, aspectBizData, sendSmsCode.lock(), bizKey, sendSmsCode.lockSeconds());
    }

    @Override
    protected void bindCaptchaToVo(Object captchaItem, Object retVal,
                                   CaptchaService captchaService, Msg.MsgInfo msgInfo) {
        // 短信发送错误超限结果
        boolean overLimit = (Boolean) captchaItem;

        if (retVal instanceof SendSmsCodeVo) {
            ((SendSmsCodeVo) retVal).setOverLimit(overLimit);
        } else if (retVal instanceof SmsCaptchaSettable) {
            ((SmsCaptchaSettable) retVal).setOverLimit(overLimit);
        } else {
            throw new CaptchaException("接收短信验证码的Vo类型不正确");
        }
    }

    @Override
    protected AspectCaptchaBizData getAspectBizData(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SendSmsCode sendSmsCode = signature.getMethod().getAnnotation(SendSmsCode.class);
        CaptchaConfigDef configDef = getCaptchaConfigDef(sendSmsCode.bizEnum(), sendSmsCode.bizEnumName());
        CaptchaService captchaService = getCaptchaService(configDef);

        return AspectCaptchaBizData.of()
            .captchaAnnotation(sendSmsCode)
            .captchaConfigDef(configDef)
            .captchaService(captchaService);
    }

    public static void main(String[] args) {

    }
}
