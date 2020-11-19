package com.fast.starters.sms.utils;

import com.google.common.collect.Lists;

import com.fast.starters.base.exception.BaseException;
import com.fast.starters.sms.enums.KeyCreateStrategy;
import com.fast.starters.sms.enums.MsgError;
import com.fast.starters.sms.exception.CaptchaException;
import com.fast.starters.sms.service.support.CaptchaService;
import com.fast.starters.sms.support.CaptchaConfigDef;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 短信帮助工具类.
 *
 * @author bowen.yan
 * @since 2019-11-03
 */
public class SmsHelper {
    /**
     * 使用方订制业务唯一bizKey时（实现CaptchaService.getBizKey()），可使用该方法来生成业务唯一key.
     * {@link CaptchaService#getBizKey(JoinPoint, CaptchaConfigDef)}
     *
     * @param uniqueUserId 用户唯一id
     * @param inputParam   输入的参数
     * @param fields       拼成业务主键的属性名
     * @param strategy     业务主键生成策略
     * @return 最终的业务主键
     */
    public static Object getUniqueBizKey(String uniqueUserId, Object inputParam, List<String> fields,
                                         KeyCreateStrategy strategy) {
        // 用户唯一id不为空，则直接返回
        if (Objects.nonNull(uniqueUserId)) {
            return uniqueUserId;
        }

        // 将字段排序，确保获取值的顺序是固定的，以便发送短信、验证短信的模型里面的字段都是一致的
        Collections.sort(fields);
        List<Object> keyValues = Lists.newArrayList();

        // 提取能拼成主键的biz key list
        if (inputParam instanceof JoinPoint) {
            extractKeyValues(keyValues, inputParam, fields,
                (param, field) -> AspectHelper.getParamValueByJoinPoint((JoinPoint) param, field)
            );
        }
        if (CollectionUtils.isEmpty(keyValues)) {
            extractKeyValues(keyValues, inputParam, fields, ReflectionUtil::getFieldValue);
        }

        if (CollectionUtils.isEmpty(keyValues)) {
            throw new BaseException("当前业务场景无法判断，无法发送短信");
        }

        // 根据策略返回最终的uniqueBizKey
        if (KeyCreateStrategy.RETURN_JUST_HIT.equals(strategy)) {
            return keyValues.get(0);
        }

        return keyValues.stream()
            .map(Object::toString)
            .collect(Collectors.joining("_"));
    }

    private static void extractKeyValues(List<Object> retKeyValues, Object inputParam, List<String> uniqueFields,
                                         BiFunction<Object, String, Object> valueExtractor) {
        for (String uniqueField : uniqueFields) {
            Object userBizKey = valueExtractor.apply(inputParam, uniqueField);
            if (Objects.nonNull(userBizKey)) {
                retKeyValues.add(userBizKey);
            }
        }
    }

    /**
     * 检查上下文传递的参数是否为空.
     *
     * @param paramValue 参数值
     * @param errorMsg   错误信息
     */
    public static void checkParam(Object paramValue, String errorMsg) {
        if (paramValue == null || StringUtils.isBlank(paramValue.toString())) {
            throw new CaptchaException(errorMsg);
        }
    }

    /**
     * 生成保存短信信息的唯一key.
     *
     * @param params 业务参数
     * @return redisKey
     */
    public static String toBizKey(String prefix, Object... params) {
        if (StringUtils.isBlank(prefix)) {
            throw new CaptchaException("发送短信业务前缀key不能为空！");
        }

        return String.format("%s_%s",
            prefix,
            Arrays.stream(params)
                .map(Object::toString)
                .collect(Collectors.joining("_"))
        );
    }

    public static void main(String[] args) {
        // 运行结果：fast123456_BJ_BANK_RECHARGE_FAIL_CHECK_IMG_CODE
        System.out.println(toBizKey("fast123456", "BJ_BANK", "RECHARGE", MsgError.FAIL_CHECK_IMG_CODE));
    }
}
