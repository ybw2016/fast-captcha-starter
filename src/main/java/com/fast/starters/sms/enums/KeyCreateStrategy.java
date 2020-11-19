package com.fast.starters.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 主键生成策略.
 *
 * @author bowen.yan
 * @since 2020-06-01
 */
@Getter
@AllArgsConstructor
public enum KeyCreateStrategy {
    RETURN_JUST_HIT("有一个字段不为空即返回"),
    COMBINE_ALL("读取所有不为空的字段后才返回");

    private String desc;
}
