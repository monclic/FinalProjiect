package com.mon.fpc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author clic
 * @date 2023-05-16 21:24
 * @description:
 */
@Data
public class LongsPublishDTO {
    private String text;

    @ApiModelProperty("以空格分隔")
    private String tags;

    private Integer type;
}
