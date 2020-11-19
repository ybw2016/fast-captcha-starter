package com.fast.starters.sms.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * 给切面提供请求信息.
 *
 * @since 2019-09-17
 */
@Slf4j
public class AspectHelper {

    /**
     * 根据请求参数名称获取参数值.
     *
     * @param point     切点
     * @param paramName 参数名称
     * @return value
     */
    public static Object getParamValueByJoinPoint(JoinPoint point, String paramName) {
        ServletRequestAttributes servletWebRequest =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(servletWebRequest)) {
            log.info("请求servletWebRequest为空，paramName: {}", paramName);
            return null;
        }
        HttpMethod httpMethod = HttpMethod.resolve(servletWebRequest.getRequest().getMethod());
        if (httpMethod == HttpMethod.POST) {
            Object argObject = point.getArgs()[0];
            return ReflectionUtil.getFieldValue(argObject, paramName);
        } else if (httpMethod == HttpMethod.GET || httpMethod == HttpMethod.HEAD) {
            Map<String, Object> reqMap = getReqParam(point);
            return reqMap.get(paramName);
        } else {
            log.info("切面不支持的请求类型：{}", httpMethod);
            return null;
        }
    }

    private static Map<String, Object> getReqParam(JoinPoint point) {
        Map<String, Object> reqMap = new HashMap<>();
        Object[] args = point.getArgs();
        MethodSignature signature = (MethodSignature) point.getSignature();
        String[] parameterNames = signature.getParameterNames();
        IntStream.range(0, args.length)
            .forEach(index -> reqMap.put(parameterNames[index], args[index]));
        return reqMap;
    }
}
