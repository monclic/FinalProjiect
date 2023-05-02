package com.mon.fpc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mon.fpc.core.Resp;
import lombok.Data;

/**
 * @author clic
 * @date 2023-05-02 15:53
 * @description:
 */
@Data
public class LoginVO extends Resp {
    @JsonProperty(value = "Token")
    private  String Token;
}
