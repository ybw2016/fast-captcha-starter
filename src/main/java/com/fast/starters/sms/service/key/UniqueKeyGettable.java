package com.fast.starters.sms.service.key;

import com.fast.starters.sms.support.CaptchaConfigDef;

/**
 * 使用方业务类中获取唯一key.
 * inputParam: {@link org.aspectj.lang.JoinPoint}、{@link com.fast.starters.base.def.rest.ReqForm}
 * bizName: {@link CaptchaConfigDef#getBizName()}
 *
 * @author bowen.yan
 * @since 2020-06-03
 */
public interface UniqueKeyGettable {
    String getBizKey(Object inputParam, String bizName);
}
