package com.mon.fpc.controller;

import cn.hutool.crypto.digest.BCrypt;
import com.entity.User;
import com.mon.fpc.Service.UserService;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.core.enums.RedisKeyEnum;
import com.mon.fpc.core.exception.BaseException;
import com.mon.fpc.dto.RegisterDTO;
import com.mon.fpc.utils.JwtUtil;
import com.mon.fpc.utils.LoginUserHolder;
import com.mon.fpc.utils.ParamUtil;
import com.mon.fpc.utils.VerifyUtils;
import com.mon.fpc.utils.redis.RedisUtil;
import com.mon.fpc.vo.LoginVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author clic
 * @date 2023-05-01 20:09
 * @description:
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    JwtUtil jwtUtil;

    @ApiOperation(value = "账号登录")
    @PostMapping("/AccountLogin")
    public Resp AccountLogin(String email,String pwd) {
        if (ParamUtil.isBlank(email)) {
            throw new BaseException("用户名不能为空");
        }
        if (ParamUtil.isBlank(pwd)) {
            throw new BaseException("验证码不能为空");
        }

        //检验是否是手机号或邮箱
        VerifyUtils.getTypeOfUserName(email);

        User user = this.userService.lambdaQuery()
                .eq(User::getUserEmail, email)
                .one();
        if (user != null) {
            if (!BCrypt.checkpw(pwd, user.getUserPwd())) {
                throw new BaseException("密码错误");
            }
            String token = jwtUtil.createToken(user.getUserId().toString(),user.getUserPwd());
            LoginVO res = new LoginVO();
            res.setToken(token);

            this.loginOut(user.getUserId().toString());
            //设置新token
            RedisUtil.set(RedisKeyEnum.USER_TOKEN,  user.getUserId().toString(), token,15, TimeUnit.DAYS);
            RedisUtil.set(RedisKeyEnum.USER_TOKEN_INDEX, user.getUserId().toString(), token, 15, TimeUnit.DAYS);
//            LoginUserHolder.set(customer);
//            System.out.println(LoginUserHolder.get(Customer.class).getId());


            return success(res);
        }
        return error("用户名或密码错误!");
    }

    @ApiOperation(value = "账号登出")
    @PostMapping("/LoginOut")
    public Resp loginOut(ServletRequest servletRequest) {
        String token = ((HttpServletRequest) servletRequest).getHeader("token");
        Claims claims = null;
        String userId;

        try {
            //token过期后，会抛出ExpiredJwtException 异常，通过这个来判定token过期，
            claims = jwtUtil.getClaims(token);
            userId = claims.getId();
        } catch (ExpiredJwtException e) {
            userId = null;
        }

        String redisToken = "";
        if (!ParamUtil.isBlank(userId)) {
            //删除旧token
            redisToken = RedisUtil.get(RedisKeyEnum.USER_TOKEN_INDEX, userId)
                    .orElse("").toString();
            log.info("【客户端登出】用户id为：{}，旧token为：{}", userId, redisToken);
        }

        if (!ParamUtil.isBlank(redisToken)) {
            RedisUtil.delete(RedisKeyEnum.USER_TOKEN_INDEX, userId);
            RedisUtil.delete(RedisKeyEnum.USER_TOKEN, redisToken);
        }

        return success();
    }


    public void loginOut(String userId) {
        //删除旧token
        String redisToken = RedisUtil.get(RedisKeyEnum.USER_TOKEN_INDEX, userId)
                .orElse("").toString();
        log.info("【客户端登出】用户id为：{}，旧token为：{}", userId, redisToken);
        if (!ParamUtil.isBlank(redisToken)) {
            RedisUtil.delete(RedisKeyEnum.USER_TOKEN_INDEX, userId);
            RedisUtil.delete(RedisKeyEnum.USER_TOKEN, redisToken);
        }
    }

    @ApiOperation(value = "账号注册")
    @PostMapping("/Register")
    public Resp register(@Validated RegisterDTO registerDTO){
        boolean exists = userService.lambdaQuery()
                .eq(User::getUserEmail, registerDTO.getEmail())
                .exists();
        if (exists){
            throw new BaseException("该邮箱已被注册");
        }

        User user = new User();
        user.setUserEmail(registerDTO.getEmail());
        user.setUserPwd(BCrypt.hashpw(registerDTO.getPwd()));
        userService.save(user);
        return success();
    }
}
