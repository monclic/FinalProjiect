package com.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author clic
 * @since 2023-05-02 02:42:51
 */
@Getter
@Setter
@TableName("user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @TableField("user_nickname")
    private String userNickName;

    @TableField("user_email")
    private String userEmail;

    @TableField("user_pwd")
    private String userPwd;

    @TableField("user_avatar")
    private String userAvatar;

    @TableField("user_signature")
    private String userSignature;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private String createTime;


}
