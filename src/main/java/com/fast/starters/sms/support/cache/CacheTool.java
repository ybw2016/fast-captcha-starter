package com.fast.starters.sms.support.cache;

/**
 * 缓存工具类.
 * 缺省redis实现
 * 用户想用自己的缓存工具，可以实现该接口
 *
 * @author bowen.yan
 * @since 2020-06-01
 */
public interface CacheTool {
    String getValue(String key);

    <T> T getValue(String key, Class<T> clazz);

    void setValue(String key, Object value, long expire);

    Integer getInteger(String key);

    void setInteger(String key, Integer value, long expire);

    Boolean hasKey(String key);

    void delete(String key);
}
