package com.mon.fpc.dto;

import lombok.Data;

/**
 * @author clic
 * @date 2023-05-19 21:57
 * @description:
 */
@Data
public class ChangeEmailDTO {
    private String oldEmail;

    private String newEmail;

    private String oldCode;

    private String newCode;
}
