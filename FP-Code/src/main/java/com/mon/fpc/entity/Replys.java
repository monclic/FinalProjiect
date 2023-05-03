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
 * 回复相关
 * </p>
 *
 * @author clic
 * @since 2023-05-03 04:03:33
 */
@Getter
@Setter
@TableName("replys")
@ApiModel(value = "Replys对象", description = "回复相关")
public class Replys {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Integer userId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private String createTime;

    @TableField("reply_context")
    private String replyContext;

    @ApiModelProperty("该内容对应的类型:0-用户评论(回复用户时使用） 1-Longs 2-Shorts")
    @TableField("to_context_type")
    private Integer toContextType;

    @ApiModelProperty("当回复用户时此处为reply_id")
    @TableField("to_context_id")
    private Integer toContextId;


}
