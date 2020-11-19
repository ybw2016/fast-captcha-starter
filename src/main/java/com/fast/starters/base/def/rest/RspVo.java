package com.fast.starters.base.def.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * rest接口统一返回出参基类.
 *
 * @author bowen.yan
 * @since 2019-07-31
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RspVo implements Serializable {
}