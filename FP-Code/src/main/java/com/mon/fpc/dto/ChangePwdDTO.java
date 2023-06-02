package com.mon.fpc.dto;

import lombok.Data;

/**
 * @author clic
 * @date 2023-05-19 22:07
 * @description:
 */
@Data
public class ChangePwdDTO {
    private String oldPwd;
    private String newPwd;
}
