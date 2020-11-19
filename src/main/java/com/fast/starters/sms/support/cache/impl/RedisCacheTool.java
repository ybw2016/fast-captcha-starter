package com.fast.starters.sms.support.cache.impl;

import com.fast.starters.sms.support.cache.CacheTool;

/**
 * 缺省缓存工具实现类（依赖Redis）.
 *
 * @author bowen.yan
 * @since 2020-06-01
 */
public class RedisCacheTool implements CacheTool {
    @Override
    public String getValue(String key) {
        return RedisTool.getValue(key);
    }

    @Override
    public <T> T getValue(String key, Class<T> clazz) {
        return RedisTool.getValue(key, clazz);
    }

    @Override
    public void setValue(String key, Object value, long expire) {
        RedisTool.setValue(key, value, expire);
    }

    @Override
    public Integer getInteger(String key) {
        return RedisTool.getInteger(key);
    }

    @Override
    public void setInteger(String key, Integer value, long expire) {
        RedisTool.setInteger(key, value, expire);
    }

    @Override
    public Boolean hasKey(String key) {
        return RedisTool.hasKey(key);
    }

    @Override
    public void delete(String key) {
        RedisTool.delete(key);
    }
}
