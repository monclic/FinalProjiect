package com.mon.fpc.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * 
 * </p>
 *
 * @author clic
 * @since 2023-05-03 11:07:24
 */
@Getter
@Setter
@TableName("shorts")
@ApiModel(value = "Shorts对象", description = "")
public class Shorts {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField("short_content")
    private String shortContent;

    @ApiModelProperty("0：未发布；1：已发布")
    @TableField("article_status")
    private Integer articleStatus;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;

    @ApiModelProperty("保存图片地址，用 空格 分隔")
    @TableField("images")
    private String images;

    @ApiModelProperty("点赞数")
    @TableField("likes")
    private Integer likes;

    @ApiModelProperty("回复数")
    @TableField("replys")
    private Integer replys;
}
