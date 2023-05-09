package com.mon.fpc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author clic
 * @date 2023-05-03 16:14
 * @description:
 */
//参考 Difference between @NotNull, @NotEmpty and @NotBlank. https://www.javapedia.net/Hibernate-interview-questions/373
@Data
public class ReplyDTO {
    @ApiModelProperty("该内容对应的类型:0-用户评论(回复用户时使用） 1-Longs 2-Shorts")
    @NotNull
    private Integer toContextType;

    @ApiModelProperty("当回复用户时此处为reply_id，楼中互相回复时此处为楼主id，回复只记录当时@的昵称，不记录id")
    @NotNull
    private Integer toContextId;

    @NotBlank
    private String replyContext;
}
