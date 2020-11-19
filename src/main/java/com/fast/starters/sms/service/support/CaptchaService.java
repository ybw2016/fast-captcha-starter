package com.fast.starters.sms.service.support;

import com.fast.starters.sms.data.Msg;
import com.fast.starters.sms.support.CaptchaConfigDef;
import com.fast.starters.sms.vo.MakeImageCodeVo;
import com.fast.starters.sms.vo.MakeTokenCodeVo;
import com.fast.starters.sms.vo.SendSmsCodeVo;
import org.aspectj.lang.JoinPoint;

/**
 * 验证码接口定义.
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
public interface CaptchaService {
    /**
     * 验证码场景key.
     * 一般来说：userId_业务场景即可代表当前业务的唯一号，可替代每次请求返回给端的令牌方式
     * 分布式锁可锁该唯一业务id即可代表当前会话
     *
     * @param joinPoint        切面
     * @param captchaConfigDef 验证码场景配置
     * @return 场景key
     */
    String getBizKey(JoinPoint joinPoint, CaptchaConfigDef captchaConfigDef);

    /**
     * 生成验证码.
     * 1. 生成短信验证码： 返回布尔值true/false，最终在vo使用 {@link SendSmsCodeVo}
     * 2. 生成图片验证码：{@link MakeImageCodeVo}
     * 3. 生成token验证码：{@link MakeTokenCodeVo}
     */
    Object makeCaptcha(Msg.MsgInfo msgInfo);

    /**
     * 校验验证码.
     */
    void checkCaptcha(Msg.MsgInfo msgInfo);
}
