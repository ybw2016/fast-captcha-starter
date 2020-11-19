package com.fast.starters.sms.vo.setter;

/**
 * 生成的图片验证码绑定器.
 * 没有走RspVo体系，则可实现此接口，以便接收返回的短信验证码
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
public interface ImageCaptchaSettable {
    /**
     * 设置图片验证码base64.
     *
     * @param imageBase64Text 生成的图片验证码base64值d
     */
    void setImageBase64Text(String imageBase64Text);
}
