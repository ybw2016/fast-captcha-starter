package com.fast.starters.sms.support.cache;

/**
 * 分布式锁接口.
 * 缺省依赖fast-starter-cache，背后采用setnx()机制
 * 支持用户方实现自己的分布式锁工具
 *
 * @author bowen.yan
 * @since 2020-06-01
 */
public interface LockTool {
    /**
     * 并发时排队锁.
     * 重试间隔：取默认值100ms
     * 重试次数：重次时间（秒）* 1000 / 重试间隔（毫秒） + 1
     *
     * @param lockKey     key
     * @param lockSeconds 获取锁的最大时间
     * @return boolean
     */
    boolean synchronizedLock(String lockKey, int lockSeconds);

    /**
     * 解锁操作.
     */
    void unlock(String lockKey);
}
