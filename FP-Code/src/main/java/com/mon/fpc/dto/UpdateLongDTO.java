package com.mon.fpc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author clic
 * @date 2023-05-20 19:50
 * @description:
 */
@Data
public class UpdateLongDTO {
    private String lid;

    private String text;

    @ApiModelProperty("以空格分隔")
    private String tags;

    private Integer type;
}
