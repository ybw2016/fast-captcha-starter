package com.fast.starters.sms.aop.token;

import com.fast.starters.sms.annotation.token.MakeTokenCode;
import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.exception.CaptchaException;
import com.fast.starters.sms.service.AbstractTokenCaptchaService;
import com.fast.starters.sms.service.support.CaptchaService;
import com.fast.starters.sms.vo.MakeTokenCodeVo;
import com.fast.starters.sms.vo.setter.TokenCaptchaSettable;
import com.fast.starters.sms.aop.MakeCaptchaAspectBase;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * 生成token切面类.
 * 通过切用户自定义注解，从而实现生成token功能
 *
 * @author bowen.yan
 * @since 2019-09-16
 */
@Slf4j
@Aspect
public class MakeTokenAspect extends MakeCaptchaAspectBase {
    @Pointcut("@annotation(com.fast.starters.sms.annotation.token.MakeTokenCode)")
    public void makeTokenCodePointcut() {
    }

    /**
     * 生成token前的业务逻辑拦截.
     * 防重复提交支持
     */
    @Around("makeTokenCodePointcut()")
    public Object lockAndExecuteBizBeforeMakeToken(ProceedingJoinPoint joinPoint) {
        AspectCaptchaBizData aspectBizData = getAspectBizData(joinPoint);
        MakeTokenCode makeTokenCode = (MakeTokenCode) aspectBizData.captchaAnnotation();
        String bizKey = aspectBizData.captchaService().getBizKey(joinPoint, aspectBizData.captchaConfigDef());

        return lockAndExecuteBiz(joinPoint, aspectBizData, makeTokenCode.lock(), bizKey, makeTokenCode.lockSeconds());
    }

    @Override
    protected void bindCaptchaToVo(Object captchaItem, Object retVal,
                                   CaptchaService captchaService, Msg.MsgInfo msgInfo) {
        // 生成的token验证码
        String token = (String) captchaItem;

        // 将token种到header中（默认）
        if (((AbstractTokenCaptchaService) captchaService).isTokenAppendToHeader()) {
            HttpServletResponse httpServletResponse =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            httpServletResponse.addHeader(CaptchaKey.TOKEN.getCode(), token);
        } else {
            if (retVal instanceof MakeTokenCodeVo) {
                ((MakeTokenCodeVo) retVal).setToken(token);
            } else if (retVal instanceof TokenCaptchaSettable) {
                ((TokenCaptchaSettable) retVal).setToken(token);
            } else {
                throw new CaptchaException("接收token验证码的Vo类型不正确");
            }
        }
    }

    @Override
    protected AspectCaptchaBizData getAspectBizData(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        MakeTokenCode makeToken = signature.getMethod().getAnnotation(MakeTokenCode.class);
        CaptchaConfigDef configDef = getCaptchaConfigDef(makeToken.bizEnum(), makeToken.bizEnumName());
        CaptchaService captchaService = getCaptchaService(configDef);

        return AspectCaptchaBizData.of()
            .captchaAnnotation(makeToken)
            .captchaConfigDef(configDef)
            .captchaService(captchaService);
    }
}
