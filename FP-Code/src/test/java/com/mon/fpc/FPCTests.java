package com.mon.fpc;

import cn.hutool.crypto.digest.BCrypt;
import com.mon.fpc.dto.RegisterDTO;
import com.mon.fpc.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * @author clic
 * @date 2023-05-02 16:10
 * @description:
 */

/**
 *  报错：no test were found
 *  原因：
 *    @Test
 *     int CryptPwd(){
 *         int a=1;
 *         return a+1;
 *     }
 *     这样的程序，没有任何的输出步骤
 *     改正：添加输出操作或其他具体的对外操作，如return改为 System.out.println()
 */
@SpringBootTest
public class FPCTests {

    @Resource
    JwtUtil jwtUtill;
    @Test
    void c(){
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("1@1.com");
        registerDTO.setPwd("1");
       b(registerDTO);
    }
    void b(@Validated RegisterDTO registerDTO){
        System.out.println(registerDTO.getPwd());
    }
}
