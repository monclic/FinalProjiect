package com.mon.fpc.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author clic
 * @date 2023-05-04 15:18
 * @description:
 */
@Data
public class LoginDTO {
    String email;
    String pwd;
}
