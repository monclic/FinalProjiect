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
 * @since 2023-05-16 09:20:37
 */
@Getter
@Setter
@TableName("tags")
@ApiModel(value = "Tags对象", description = "")
public class Tags {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("tag_name")
    private String tagName;

    @TableField("longs_sum")
    private Integer longsSum;


}
