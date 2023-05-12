package com.mon.fpc.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


/**
 * @author clic
 * @date 2023-05-02 17:10
 * @description:
 */
// @Validated相关注解参考
// https://blog.csdn.net/danielzhou888/article/details/74740817
// https://blog.51cto.com/u_3664660/5070629
@Data
public class RegisterDTO {
    @Email(message = "请检查email地址")
    @NotBlank(message = "请输入email地址")
    private String email;

    @NotBlank(message = "请输入密码")
    @Length(message = "密码为{min} 到 {max} 位",min=1 ,max = 18)
    private String pwd;

    private String code;
}
