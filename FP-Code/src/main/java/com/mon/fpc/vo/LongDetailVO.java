package com.mon.fpc.vo;

import com.mon.fpc.core.Resp;
import lombok.Data;

/**
 * @author clic
 * @date 2023-05-16 21:39
 * @description:
 */
@Data
public class LongDetailVO extends Resp {
    private Integer id;

    private String username;

    private String avatar;

    private String[] tags;

    private String text;

    private String createTime;

    private Integer likes;

    private Integer replys;

    private boolean likeIs;
}
