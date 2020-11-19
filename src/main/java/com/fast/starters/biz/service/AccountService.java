package com.fast.starters.biz.service;

import com.fast.starters.biz.request.OpenAccountForm;
import com.fast.starters.biz.request.SmsConfirmForm;
import com.fast.starters.biz.response.OpenAccountRspVo;
import com.fast.starters.sms.vo.SendSmsCodeVo;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

/**
 * @author bw
 * @since 2020-11-18
 */
@Service
public class AccountService {
    public SendSmsCodeVo openAccount(OpenAccountForm openAccountForm) {
        throw new NotImplementedException("openAccount");
    }

    public OpenAccountRspVo openAccountConfirm(SmsConfirmForm form) {
        throw new NotImplementedException("openAccountConfirm");
    }
}
