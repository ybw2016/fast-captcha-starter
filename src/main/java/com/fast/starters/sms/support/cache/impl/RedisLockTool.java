package com.fast.starters.sms.support.cache.impl;

import com.fast.starters.sms.support.cache.LockTool;
import org.apache.commons.lang3.NotImplementedException;

/**
 * 缺省分布式排队锁工具实现类（依赖Redis setnx()）.
 *
 * @author bowen.yan¬
 * @since 2020-06-01
 */
public class RedisLockTool implements LockTool {
    //@Autowired
    //private RedisAtomicLock redisAtomicLock;

    @Override
    public boolean synchronizedLock(String lockKey, int lockSeconds) {
        //return redisAtomicLock.synchronizedLock(lockKey, lockSeconds);
        throw new NotImplementedException("需引入redis分布式锁");
    }

    @Override
    public void unlock(String lockKey) {
        //redisAtomicLock.unlock(lockKey);
        throw new NotImplementedException("需引入redis分布式锁");
    }
}
