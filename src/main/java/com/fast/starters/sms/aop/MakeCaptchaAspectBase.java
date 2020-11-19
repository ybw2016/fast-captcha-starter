package com.fast.starters.sms.aop;

import com.fast.starters.base.exception.BaseException;
import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.exception.CaptchaException;
import com.fast.starters.sms.model.support.AspectCaptchaBizData;
import com.fast.starters.sms.service.support.AbstractCaptchaService;
import com.fast.starters.sms.service.support.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 生成验证码切面基类.
 *
 * @author bowen.yan
 * @since 2019-12-02
 */
@Slf4j
public abstract class MakeCaptchaAspectBase extends CaptchaAspectBase {
    protected abstract AspectCaptchaBizData getAspectBizData(JoinPoint joinPoint);

    protected abstract void bindCaptchaToVo(Object captchaItem, Object retVal,
                                            CaptchaService captchaService, Msg.MsgInfo msgInfo);

    protected Object lockAndExecuteBiz(ProceedingJoinPoint point, AspectCaptchaBizData aspectBizData,
                                       boolean lock, String lockBizKey, int lockSeconds) {
        Object result;
        boolean lockSuccess = false;
        try {
            if (lock) {
                lockSuccess = lockTool.synchronizedLock(lockBizKey, lockSeconds);
                log.info("doCaptchaLockBiz -> lockBizKey:{}, lockSuccess:{}", lockBizKey, lockSuccess);
            }

            if (!lockSuccess) {
                throw new RuntimeException("获取业务锁超时，无法执行业务");
            }

            // 执行业务逻辑
            result = point.proceed();

            // 执行成功后就发送验证码
            makeCaptchaAfterBiz(point, result, aspectBizData);
        } catch (Throwable t) {
            log.error("生成验证码时业务执行异常", t);

            // 业务验证异常原样抛出
            if (t instanceof BaseException) {
                BaseException ex = (BaseException) t;
                throw new CaptchaException(ex.getCode(), ex.getMsg());
            }
            // 其他为验证异常
            throw new CaptchaException("生成验证码时业务执行异常");
        } finally {
            // 如果没抢到锁还删除业务，会影响处理中的业务
            if (lockSuccess) {
                lockTool.unlock(lockBizKey);
            }
        }
        return result;
    }

    protected void makeCaptchaAfterBiz(JoinPoint joinPoint, Object retVal, AspectCaptchaBizData aspectBizData) {
        AbstractCaptchaService captchaService = (AbstractCaptchaService) aspectBizData.captchaService();
        Msg.MsgInfo msgInfo = captchaService.buildMsgInfo(joinPoint, aspectBizData.captchaConfigDef());

        // 生产验证码
        Object captchaItem = captchaService.makeCaptcha(msgInfo);

        // 绑定验证码码到返回值
        bindCaptchaToVo(captchaItem, retVal, captchaService, msgInfo);
    }
}
