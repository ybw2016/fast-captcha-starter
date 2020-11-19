package com.fast.starters.sms.vo;

import com.fast.starters.base.def.rest.RspVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 返回带token的vo.
 *
 * @author bowen.yan
 * @since 2019-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MakeTokenCodeVo extends RspVo {
    private String token;//token唯一流水号
}
