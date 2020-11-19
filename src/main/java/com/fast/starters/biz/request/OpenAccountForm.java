package com.fast.starters.biz.request;

import com.fast.starters.base.def.rest.ReqForm;
import lombok.Data;

/**
 * @author bw
 * @since 2020-11-18
 */
@Data
public class OpenAccountForm extends ReqForm {
    private String userId;
    private String userName;
    private String bankCardNo;
    private String mobile;
}
