package com.fast.starters.biz.controller;

import com.fast.starters.biz.enums.SmsType;
import com.fast.starters.biz.request.OpenAccountForm;
import com.fast.starters.biz.request.SmsConfirmForm;
import com.fast.starters.biz.response.OpenAccountRspVo;
import com.fast.starters.biz.service.AccountService;
import com.fast.starters.sms.annotation.sms.CheckSmsCode;
import com.fast.starters.sms.annotation.sms.SendSmsCode;
import com.fast.starters.sms.vo.SendSmsCodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author bw
 * @since 2020-11-18
 */
@Slf4j
@RestController
@RequestMapping("/sms/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @SendSmsCode(bizEnum = SmsType.class, bizEnumName = "OPEN_ACCOUNT", lockSeconds = 20)
    @PostMapping(value = "/open-account")
    public SendSmsCodeVo openAccount(@Valid @RequestBody OpenAccountForm openAccountForm) {
        return accountService.openAccount(openAccountForm);
    }

    @CheckSmsCode(bizEnum = SmsType.class, bizEnumName = "OPEN_ACCOUNT")
    @PostMapping(value = "/open-account/confirm")
    public OpenAccountRspVo openAccountConfirm(@Valid @RequestBody SmsConfirmForm smsConfirmForm) {
        return accountService.openAccountConfirm(smsConfirmForm);
    }
}
