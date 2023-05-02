package com.mon.fpc.core;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author Created by ll on 2022-08-22
 * @description
 */
@Data

public class Resp  {
    @ApiModelProperty("结果")
    @JsonProperty(value = "Result")
    private boolean Result;

    @ApiModelProperty("错误码")
    @JsonProperty(value = "ErrorCode")
    private String ErrorCode;

    @JsonProperty(value = "ErrorMsg")
    @ApiModelProperty("错误信息")
    private String ErrorMsg;

    @Getter
    @AllArgsConstructor
    public enum RespEnum implements RespKeyInterface {
        NO_LOGIN(401, "尚未登录"),
        PARAM_ERROR(412, "参数错误"),
        NO_FOUND(415, "未找到相关数据"),
        INNER_ERROR(500, "系统内部错误"),
        NO_RIGHT(401,"权限不足");
        private Integer code;
        private String keyDescription;
    }
}
