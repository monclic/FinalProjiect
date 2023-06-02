package com.mon.fpc.controller;


import cn.hutool.crypto.digest.BCrypt;
import com.mon.fpc.Service.AdminsService;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.core.exception.BaseException;
import com.mon.fpc.dto.AdminAddDTO;
import com.mon.fpc.dto.AdminLoginDTO;
import com.mon.fpc.entity.Admins;
import com.mon.fpc.utils.JwtUtil;
import com.mon.fpc.utils.LoginUserHolder;
import com.mon.fpc.vo.CommonListVO;
import com.mon.fpc.vo.LoginVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author clic
 * @since 2023-05-23 01:16:05
 */
@RestController
@RequestMapping("/admin")
public class AdminsController extends BaseController {

    @Resource
    private AdminsService adminsService;
    @Resource
    private JwtUtil jwtUtil;

    @ApiOperation(value = "管理员登录")
    @PostMapping("/AccountLogin")
    private Resp login(@RequestBody AdminLoginDTO adminLoginDTO) {
        Admins one = adminsService.lambdaQuery()
                .eq(Admins::getId, adminLoginDTO.getId())
                .one();
        if (one != null) {
            if (!BCrypt.checkpw(adminLoginDTO.getPwd(), one.getPwd())) {
                throw new BaseException("密码错误");
            }
            String token = jwtUtil.createToken(one.getId().toString(), one.getPwd());

            LoginVO res = new LoginVO();
            res.setToken(token);
            LoginUserHolder.set(one);

            return success(res);
        }

        return error("用户名或密码错误!");

    }

    @ApiOperation(value = "")
    @PostMapping("/add")
    private Resp add(@RequestBody AdminAddDTO adminAddDTO) {

        Admins news = new Admins();
        news.setAdminName(adminAddDTO.getAdminName());
        news.setPwd(BCrypt.hashpw(adminAddDTO.getPwd()));

        adminsService.save(news);
        return success();
    }

    @ApiOperation(value = "")
    @GetMapping("/getAdmin")
    private Resp getAdmin() {
        List<Admins> list = adminsService.lambdaQuery()
                .list();
        CommonListVO<Admins> adminsCommonListVO = new CommonListVO<>();
        adminsCommonListVO.setList(list);
        return success(adminsCommonListVO);
    }
}

