package com.mon.fpc.vo;

import com.mon.fpc.core.Resp;
import lombok.Data;

/**
 * @author clic
 * @date 2023-05-19 20:48
 * @description:
 */
@Data
public class InfoVO extends Resp {
    private String avatar;
    private String username;
    private String email;
    private String uid;
}
