package com.mon.fpc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 点赞相关
 * </p>
 *
 * @author clic
 * @since 2023-05-03 04:03:33
 */
@Getter
@Setter
@TableName("likes")
@ApiModel(value = "Likes对象", description = "点赞相关")
public class Likes {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("该内容对应的类型:1-Longs 2-Shorts")
    @TableField("context_type")
    private Integer contextType;

    @ApiModelProperty("查看的内容的id")
    @TableField("context_id")
    private Integer contextId;


}
