package com.mon.fpc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author clic
 * @date 2023-06-10 15:36
 * @description:
 */
@Data
public class ChatCache {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("text")
    private String text;
}
