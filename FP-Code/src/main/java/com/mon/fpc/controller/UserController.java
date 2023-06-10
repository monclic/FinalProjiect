package com.mon.fpc.controller;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.entity.User;
import com.mon.fpc.Service.UserService;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.core.enums.RedisKeyEnum;
import com.mon.fpc.core.exception.BaseException;
import com.mon.fpc.dto.ChangeEmailDTO;
import com.mon.fpc.dto.ChangePwdDTO;
import com.mon.fpc.dto.LoginDTO;
import com.mon.fpc.dto.RegisterDTO;
import com.mon.fpc.utils.*;
import com.mon.fpc.utils.redis.RedisUtil;
import com.mon.fpc.vo.CommonTypeVo;
import com.mon.fpc.vo.InfoVO;
import com.mon.fpc.vo.LoginVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
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
    @Resource
    MailUtils mailUtils;

    @ApiOperation(value = "账号登录")
    @PostMapping("/AccountLogin")
    public Resp AccountLogin(@RequestBody LoginDTO loginDTO) {
        String email = loginDTO.getEmail();
        String pwd = loginDTO.getPwd();
        if (ParamUtil.isBlank(email)) {
            throw new BaseException("email不能为空");
        }
        if (ParamUtil.isBlank(pwd)) {
            throw new BaseException("密码不能为空");
        }

        //检验是否是邮箱
        VerifyUtils.getTypeOfUserName(email);

        User user = this.userService.lambdaQuery()
                .eq(User::getUserEmail, email)
                .one();
        if (user != null) {
            if (!BCrypt.checkpw(pwd, user.getUserPwd())) {
                throw new BaseException("密码错误");
            }
            String token = jwtUtil.createToken(user.getUserId().toString(), user.getUserPwd());
            LoginVO res = new LoginVO();
            res.setToken(token);

            this.loginOut(user.getUserId().toString());
            //设置新token
            RedisUtil.set(RedisKeyEnum.USER_TOKEN, user.getUserId().toString(), token, 15, TimeUnit.DAYS);
            LoginUserHolder.set(user);
//            System.out.println(LoginUserHolder.get(User.class).getUserId());


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
            redisToken = RedisUtil.get(RedisKeyEnum.USER_TOKEN, userId)
                    .orElse("").toString();
            log.info("【客户端登出】用户id为：{}，旧token为：{}", userId, redisToken);
        }

        if (!ParamUtil.isBlank(redisToken)) {
            RedisUtil.delete(RedisKeyEnum.USER_TOKEN, redisToken);
        }

        return success();
    }


    public void loginOut(String userId) {
        //删除旧token
        String redisToken = RedisUtil.get(RedisKeyEnum.USER_TOKEN, userId)
                .orElse("").toString();
        log.info("【客户端登出】用户id为：{}，旧token为：{}", userId, redisToken);
        if (!ParamUtil.isBlank(redisToken)) {
            RedisUtil.delete(RedisKeyEnum.USER_TOKEN, redisToken);
        }
    }

    @ApiOperation(value = "账号注册")
    @PostMapping("/Register")
    public Resp register(@RequestBody @Validated RegisterDTO registerDTO) {
        boolean exists = userService.lambdaQuery()
                .eq(User::getUserEmail, registerDTO.getEmail())
                .exists();
        if (exists) {
            throw new BaseException("该邮箱已被注册");
        }

        Optional<Object> o = RedisUtil.get(RedisKeyEnum.USER_REGISTER_CODE, registerDTO.getEmail());
        if (!o.isPresent()){
            return error("请获取验证码");
        }
        String code = (String) o.get();
        if (!code.equals(registerDTO.getCode()))return error("请确认验证码");
        RedisUtil.delete(RedisKeyEnum.USER_REGISTER_CODE,registerDTO.getEmail());

        User user = new User();
        user.setUserEmail(registerDTO.getEmail());
        //TODO  加盐
        user.setUserPwd(BCrypt.hashpw(registerDTO.getPwd()));
        userService.save(user);
        Integer userId = userService.lambdaQuery()
                .select(User::getUserId)
                .eq(User::getUserEmail, registerDTO.getEmail())
                .one().getUserId();
        String name="用户"+userId.toString();
        userService.lambdaUpdate()
                .set(User::getUserNickName,name)
                .eq(User::getUserId,userId)
                .update();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(registerDTO.getEmail());
        loginDTO.setPwd(registerDTO.getPwd());
        return AccountLogin(loginDTO);
    }
    @ApiOperation(value = "发送验证码 注册用")
    @PostMapping("/sendCode")
    public Resp sendCode(@RequestBody String email){
        boolean checkEmail = VerifyUtil.checkEmail(email);
        if (!checkEmail) return error("请输入正确的email地址");
        boolean exists = userService.lambdaQuery()
                .eq(User::getUserEmail, email)
                .exists();
//        TODO
//        if (exists) {
//            throw new BaseException("该邮箱已被注册");
//        }

        //设置验证码
        String random = RandomUtil.randomNumbers(6);
        RedisUtil.set(RedisKeyEnum.USER_REGISTER_CODE, email, random, 5, TimeUnit.MINUTES);
        ThreadUtil.execAsync(() -> {
            mailUtils.sendCodeMail(email, random);
        });
        return success();
    }
    @RequestMapping("/Upload")
    public String Upload(@RequestParam MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        int index = name.indexOf(".");
        String suffix = name.substring(index);
        if (suffix.equalsIgnoreCase(".png")) {
            suffix = ".png";
        } else if (suffix.equalsIgnoreCase(".jpg")) {
            suffix = ".jpg";
        }
        //防止文件名称重复造成文件覆盖
        String timeStamp = new SimpleDateFormat("yyyMMddHHmmss").format(new Date());
        String newName = name.substring(0, index) + "_" + timeStamp + suffix;

        String destFilePath = "C:\\Users\\17272\\Desktop\\imgs\\"+ newName;

        //保存图片
        file.transferTo(new File(destFilePath));
//        System.out.println(newName);
//        userMapper.updateAvatar(newName,uid);
        return newName;

    }

    @ApiOperation(value = "获得用户名")
    @GetMapping("/getName")
    public Resp getName(){
        User user = LoginUserHolder.get(User.class);
        if (user.getUserId()==null) return error("");
        CommonTypeVo<String> stringCommonTypeVo = new CommonTypeVo<>();
        stringCommonTypeVo.setGoods(user.getUserNickName());
        return success(stringCommonTypeVo);
    }

    @ApiOperation(value = "发送验证码 找回密码用")
    @PostMapping("/sendCode_2")
    public Resp sendCode_2(@RequestBody String email){
        boolean checkEmail = VerifyUtil.checkEmail(email);
        if (!checkEmail) return error("请输入正确的email地址");
        boolean exists = userService.lambdaQuery()
                .eq(User::getUserEmail, email)
                .exists();
        if (!exists) {
            throw new BaseException("该邮箱尚未注册");
        }

        //设置验证码
        String random = RandomUtil.randomNumbers(6);
        RedisUtil.set(RedisKeyEnum.USER_RE_SET_CODE, email, random, 5, TimeUnit.MINUTES);
        ThreadUtil.execAsync(() -> {
            mailUtils.sendCodeMail(email, random);
        });
        return success();
    }

    @ApiOperation(value = "找回密码")
    @PostMapping("/ReSetPwd")
    public Resp reSetPwd(@RequestBody @Validated RegisterDTO registerDTO) {
        boolean exists = userService.lambdaQuery()
                .eq(User::getUserEmail, registerDTO.getEmail())
                .exists();
        if (!exists) {
            throw new BaseException("该邮箱尚未注册");
        }

        Optional<Object> o = RedisUtil.get(RedisKeyEnum.USER_RE_SET_CODE, registerDTO.getEmail());
        if (!o.isPresent()){
            return error("请获取验证码");
        }
        String code = (String) o.get();
        if (!code.equals(registerDTO.getCode()))return error("请确认验证码");
        RedisUtil.delete(RedisKeyEnum.USER_REGISTER_CODE,registerDTO.getEmail());

        Integer userId = userService.lambdaQuery()
                .select(User::getUserId)
                .eq(User::getUserEmail, registerDTO.getEmail())
                .one().getUserId();


        userService.lambdaUpdate()
                .set(User::getUserPwd,BCrypt.hashpw(registerDTO.getPwd()))
                .eq(User::getUserId,userId)
                .update();

        return success();
    }

    @ApiOperation(value = "个人信息")
    @GetMapping("/info")
    public Resp info() {
        User user = LoginUserHolder.get(User.class);
        if (user==null)return error("404");

        user=userService.lambdaQuery()
                .eq(User::getUserId,user.getUserId())
                .one();
        InfoVO infoVO = new InfoVO();
        infoVO.setAvatar("http://127.0.0.1/imgs/"+user.getUserAvatar());
        infoVO.setUsername(user.getUserNickName());
        infoVO.setEmail(user.getUserEmail());
        infoVO.setUid(user.getUserId().toString());

        return success(infoVO);
    }

    @ApiOperation(value = "上传头像")
    @PostMapping("/upload")
    public Resp upload(MultipartFile file) throws IOException {
        String img = ImgUtil.saveImg(file);
        userService.lambdaUpdate()
                .set(User::getUserAvatar,img)
                .eq(User::getUserId,LoginUserHolder.get(User.class).getUserId())
                .update();
        CommonTypeVo<String> stringCommonTypeVo = new CommonTypeVo<>();
        stringCommonTypeVo.setGoods(img);
        return success(stringCommonTypeVo);
    }

    @ApiOperation(value = "更换邮箱")
    @PostMapping("/changeEmail")
    public Resp changeEmail(@RequestBody ChangeEmailDTO changeEmailDTO){
        Optional<Object> oldO = RedisUtil.get(RedisKeyEnum.USER_CHANGE_EMAIL_OLD_CODE, changeEmailDTO.getOldEmail());
        Optional<Object> newO = RedisUtil.get(RedisKeyEnum.USER_CHANGE_EMAIL_NEW_CODE, changeEmailDTO.getNewEmail());

        if (!oldO.isPresent()||!newO.isPresent()){
            return error("");
        }
        String oldCOde = (String) oldO.get();
        String newCode = (String) newO.get();
        if (oldCOde.equals(changeEmailDTO.getOldCode())&&newCode.equals(changeEmailDTO.getNewCode())){
            String userEmail = LoginUserHolder.get(User.class).getUserEmail();
            if (userEmail.equals(changeEmailDTO.getOldEmail())){
                userService.lambdaUpdate()
                        .set(User::getUserEmail,changeEmailDTO.getNewEmail())
                        .eq(User::getUserEmail,userEmail)
                        .update();
            }
        }
        return success();
    }

    @ApiOperation(value = "原邮箱发验证码")
    @PostMapping("/sendCode_changeEmail")
    public Resp sendCode_changeEmail(@RequestBody String email_old){
        boolean checkEmail = VerifyUtil.checkEmail(email_old);
        if (!checkEmail) return error("请输入正确的email地址");
        boolean exists = userService.lambdaQuery()
                .eq(User::getUserEmail, email_old)
                .exists();
        if (!exists) {
            throw new BaseException("该邮箱尚未注册");
        }

        //设置验证码
        String random = RandomUtil.randomNumbers(6);
        RedisUtil.set(RedisKeyEnum.USER_CHANGE_EMAIL_OLD_CODE, email_old, random, 5, TimeUnit.MINUTES);
        ThreadUtil.execAsync(() -> {
            mailUtils.sendCodeMail(email_old, random);
        });
        return success();
    }
    @ApiOperation(value = "新邮箱换验证码")
    @PostMapping("/sendCode_changeEmail_new")
    public Resp sendCode_changeEmail_new(@RequestBody String email_new){
        boolean checkEmail = VerifyUtil.checkEmail(email_new);
        if (!checkEmail) return error("请输入正确的email地址");
        boolean exists = userService.lambdaQuery()
                .eq(User::getUserEmail, email_new)
                .exists();
        if (exists) {
            throw new BaseException("该邮箱已被注册");
        }

        //设置验证码
        String random = RandomUtil.randomNumbers(6);
        RedisUtil.set(RedisKeyEnum.USER_CHANGE_EMAIL_NEW_CODE, email_new, random, 5, TimeUnit.MINUTES);
        ThreadUtil.execAsync(() -> {
            mailUtils.sendCodeMail(email_new, random);
        });

        return success();
    }


    @ApiOperation(value = "更新昵称")
    @PostMapping("/changeUserName")
    public Resp changeUserName(@RequestBody String username_new){
        Integer userId = LoginUserHolder.get(User.class).getUserId();
        userService.lambdaUpdate()
                .set(User::getUserNickName,username_new)
                .eq(User::getUserId,userId)
                .update();
        return success();
    }

    @ApiOperation(value = "更新密码")
    @PostMapping("/changePwd")
    public Resp changePwd(@RequestBody ChangePwdDTO changePwdDTO){
        Integer userId = LoginUserHolder.get(User.class).getUserId();

        boolean exists = userService.lambdaQuery()
                .eq(User::getUserId,userId)
                .exists();
        if (!exists)return error("");

        userService.lambdaUpdate()
                .set(User::getUserPwd,BCrypt.hashpw(changePwdDTO.getNewPwd()))
                .eq(User::getUserId,userId)
                .update();
        return success();
    }
}
