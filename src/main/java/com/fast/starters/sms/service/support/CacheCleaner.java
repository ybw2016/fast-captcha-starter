package com.fast.starters.sms.service.support;

import com.fast.starters.sms.data.Msg;

/**
 * 缓存清除器.
 *
 * @author bowen.yan
 * @since 2020-06-01
 */
public interface CacheCleaner {
    /**
     * 清除发送信息时的次数等状态信息.
     *
     * @param msgInfo 信息中间载体
     */
    void clearSendTimesCache(Msg.MsgInfo msgInfo);
}
