package com.fast.starters.biz.response;

import com.fast.starters.base.def.rest.RspVo;
import lombok.Data;

/**
 * @author bw
 * @since 2020-11-18
 */
@Data
public class OpenAccountRspVo extends RspVo {
    private String bankCardNo;
    private String accountAmount;
}
