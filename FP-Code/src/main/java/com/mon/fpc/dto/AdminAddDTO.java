package com.mon.fpc.dto;

import lombok.Data;

/**
 * @author clic
 * @date 2023-05-23 13:31
 * @description:
 */
@Data
public class AdminAddDTO {
    private String adminName;
    private String pwd;
    private Integer aid;
}
