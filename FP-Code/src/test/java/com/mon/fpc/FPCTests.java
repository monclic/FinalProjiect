package com.mon.fpc;

import cn.hutool.crypto.digest.BCrypt;
import com.entity.User;
import com.mon.fpc.Service.UserService;
import com.mon.fpc.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
    @Resource
    UserService userService;

    @Test
    void b(){
        com.entity.User user = new com.entity.User();
        user.setUserEmail("111111");
        //TODO  加盐
        user.setUserPwd(BCrypt.hashpw("111111"));
//        return user;
//        userService.save(user);
//        c(user);
        System.out.println(BCrypt.hashpw("111111"));
    }
    void c(User user){
        System.out.println(user.getUserId());
    }
}
