package com.mon.fpc.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mon.fpc.core.Resp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author clic
 * @date 2023-05-09 20:42
 * @description:
 */
@Data
public class ShortDetailVO extends Resp {
    private Integer id;

    private String username;

    private String avatar;

    private String[] imagesList;

    private boolean imagesIs=false;

    private String shortContent;

    private String createTime;

    private Integer likes;

    private Integer replys;

    private boolean likeIs;
}
