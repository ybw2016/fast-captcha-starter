package com.fast.starters.sms.vo;

import com.fast.starters.base.def.rest.RspVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片验证码.
 *
 * @author hfHuang
 * @since 2019-08-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakeImageCodeVo extends RspVo {
    // 图片验证码base64
    private String img;
}
