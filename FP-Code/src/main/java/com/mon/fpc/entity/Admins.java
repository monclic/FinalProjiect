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
@TableName("admins")
@ApiModel(value = "Admins对象", description = "")
public class Admins {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("admin_name")
    private String adminName;

    @TableField("pwd")
    private String pwd;


}
