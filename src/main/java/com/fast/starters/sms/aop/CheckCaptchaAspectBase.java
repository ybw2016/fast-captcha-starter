package com.fast.starters.sms.aop;

import com.fast.starters.base.exception.BaseException;
import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.enums.LifecycleType;
import com.fast.starters.sms.exception.CaptchaException;
import com.fast.starters.sms.model.support.AspectCaptchaBizData;
import com.fast.starters.sms.model.support.BizExtItem;
import com.fast.starters.sms.model.support.ParamKeyValidateItem;
import com.fast.starters.sms.service.support.AbstractCaptchaService;
import com.fast.starters.sms.service.support.CaptchaService;
import com.fast.starters.sms.support.CaptchaConfigDef;
import com.fast.starters.sms.utils.AspectHelper;
import com.fast.starters.sms.utils.SmsHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * 验证验证码切面基类.
 *
 * @author bowen.yan
 * @since 2019-12-02
 */
@Slf4j
public abstract class CheckCaptchaAspectBase extends CaptchaAspectBase {
    protected abstract AspectCaptchaBizData getAspectBizData(JoinPoint joinPoint);

    protected abstract BizExtItem getBizExtItem(ProceedingJoinPoint joinPoint,
                                                CaptchaService captchaService,
                                                AspectCaptchaBizData aspectBizData);

    protected abstract List<ParamKeyValidateItem> getParamValueValidators();

    protected Object checkCaptchaBeforeBiz(ProceedingJoinPoint joinPoint) {
        AspectCaptchaBizData aspectBizData = getAspectBizData(joinPoint);
        CaptchaConfigDef captchaConfigDef = aspectBizData.captchaConfigDef();
        AbstractCaptchaService captchaService = getCaptchaService(captchaConfigDef);

        Msg.MsgInfo msgInfo = captchaService.buildMsgInfo(joinPoint, captchaConfigDef);
        List<ParamKeyValidateItem> paramKeyValidateItems = getParamValueValidators();
        if (CollectionUtils.isNotEmpty(paramKeyValidateItems)) {
            paramKeyValidateItems.forEach(paramKeyValidateItem -> {
                Object paramData = AspectHelper.getParamValueByJoinPoint(
                    joinPoint, paramKeyValidateItem.getCaptchaKey().getCode());
                if (StringUtils.isNotEmpty(paramKeyValidateItem.getErrorMsg())) {
                    SmsHelper.checkParam(paramData, paramKeyValidateItem.getErrorMsg());
                }
                // 绑定值
                paramKeyValidateItem.getParamValueBinder().accept(paramData, msgInfo);
            });
        }

        // 校验验证码逻辑加锁处理
        String bizKey = captchaService.getBizKey(joinPoint, captchaConfigDef);
        BizExtItem bizExtItem = getBizExtItem(joinPoint, captchaService, aspectBizData);
        Object result;
        boolean lockSuccess = false;
        try {
            if (bizExtItem.lock()) {
                lockSuccess = lockTool.synchronizedLock(bizKey, bizExtItem.lockSeconds());
                log.info("checkCaptchaBeforeBiz -> lockBizKey:{}, lockSuccess:{}", bizKey, lockSuccess);
            }

            if (!lockSuccess) {
                throw new RuntimeException("获取业务锁超时，无法执行业务");
            }

            // 校验
            captchaService.checkCaptcha(msgInfo);

            // 验证成功时立即清除发送次数等缓存，剩下的缓存key可在业务执行成功后通过clearAllCache()方法完全清除
            if (LifecycleType.CLEAR_AFTER_VERIFY_OK.equals(bizExtItem.lifecycleType())) {
                // 清除缓存(短信次数相关的缓存)
                captchaService.clearSendTimesCache(msgInfo);
            }

            // 执行业务逻辑
            result = joinPoint.proceed();

            // 清除掉缓存信息后，重复提交时无业务场景数据自动拦截
            smsCacheService.clearAllCache(msgInfo);
        } catch (Throwable t) {
            log.error("验证验证码时业务执行异常", t);

            // 业务验证异常原样抛出
            if (t instanceof BaseException) {
                BaseException ex = (BaseException) t;
                throw new CaptchaException(ex.getCode(), ex.getMsg());
            }
            // 其他为验证异常
            throw new CaptchaException("验证验证码时业务执行异常");
        } finally {
            // 如果没抢到锁还删除业务，会影响处理中的业务
            if (lockSuccess) {
                lockTool.unlock(bizKey);
            }
        }
        return result;
    }
}
