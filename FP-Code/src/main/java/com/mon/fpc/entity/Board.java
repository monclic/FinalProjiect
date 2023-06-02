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
 * 
 * </p>
 *
 * @author clic
 * @since 2023-05-23 01:16:05
 */
@Getter
@Setter
@TableName("board")
@ApiModel(value = "Board对象", description = "")
public class Board {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("text")
    private String text;

    @TableField("admin_id")
    private Integer adminId;


}
