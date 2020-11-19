package com.fast.starters.sms.data;

import com.fast.starters.sms.model.config.CaptchaConfig;
import com.fast.starters.sms.service.support.SmsCacheService;
import com.fast.starters.sms.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码数据类.
 * 本类相当于业务数据上下文
 * 使用场景：
 * 1. 发送短信验证码之前：
 * 保存业务数据到缓存 {@link SmsCacheService#saveMsgInfoToRedis(MsgInfo)}
 * <p></p>
 * 2. 验证完短信验证码之后：
 * 从缓存中读取数据 {@link SmsCacheService#getMsgInfoFromRedis(MsgInfo)}
 * <p></p>
 * 保存到缓存中的业务数据可自动清除，无需使用方手动清除：
 * {@link SmsCacheService#clearAllCache(MsgInfo)}
 *
 * @author bowen.yan
 * @since 2019-09-19
 */
public class Msg {
    /**
     * 保险验证码相关的上下文数据.
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class MsgInfo {
        private String bizKey;// 标志用户业务场景的业务唯一key   userId+业务模块+场景模块（所有模式）
        private CaptchaConfig captchaConfig; // 业务场景配置（注解模式时专用）

        private String mobile;//用户手机号（Sms短信专用）
        private String smsCode;//用户收到的短信验证码（Sms短信专用）
        private String captchaText;//图片验证码文本（Image图片专用）
        private String token;//token唯一流水号（Token模式专用）

        private Object extData;//业务数据扩展字段（所有模式）

        public <T> T getExtData(Class<T> clazz) {
            return JsonUtil.fromJson(JsonUtil.toJsonString(extData), clazz);
        }
    }

    /**
     * 短信平台返回的短信令牌、短信验证码等信息.
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class MsgInfoRet {
        private String smsCode; //短信验证码（商业短信平台可以返回此码）
        private String token; //短信令牌（银行只返令牌，不返短信验证码给合作平台）
        private String effectTime; //短信验证码有效时间
        private Object miscData;//业务数据扩展字段

        public <T> T getMiscData(Class<T> clazz) {
            return JsonUtil.fromJson(JsonUtil.toJsonString(miscData), clazz);
        }
    }
}
