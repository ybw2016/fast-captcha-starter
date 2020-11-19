package com.fast.starters.sms.vo.setter;

import com.fast.starters.sms.enums.OverLimitType;

/**
 * 生成的短信验证码绑定器.
 * 没有走RspVo体系，则可实现此接口，以便接收返回的短信验证码
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
public interface SmsCaptchaSettable {
    /**
     * 绑定是否由短信验证码切换到图片验证码.
     *
     * {@link OverLimitType#isValue()}
     */
    void setOverLimit(boolean overLimit);
}
