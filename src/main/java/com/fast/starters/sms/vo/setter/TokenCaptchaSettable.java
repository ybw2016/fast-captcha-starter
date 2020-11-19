package com.fast.starters.sms.vo.setter;

/**
 * 生成的短信验证码绑定器.
 * 没有走RspVo体系，则可实现此接口，以便接收返回的短信验证码
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
public interface TokenCaptchaSettable {
    /**
     * 绑定token到返回值vo中.
     *
     * @param token 生成的token
     */
    void setToken(String token);
}
