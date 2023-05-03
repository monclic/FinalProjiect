package com.mon.fpc.controller;


import com.entity.User;
import com.mon.fpc.Service.UserService;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.dto.ReplyDTO;
import com.mon.fpc.dto.ShortPublishDTO;
import com.mon.fpc.entity.Replys;
import com.mon.fpc.entity.Shorts;
import com.mon.fpc.utils.LoginUserHolder;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 回复相关 前端控制器
 * </p>
 *
 * @author clic
 * @since 2023-05-03 04:03:33
 */
@RestController
@RequestMapping("/replys")
public class ReplysController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private com.mon.fpc.service.ShortsService shortsService;
    @Resource
    private com.mon.fpc.service.ReplysService replysService;

    @ApiOperation(value = "回复 该内容对应的类型:0-用户评论(回复用户时使用） 1-Longs 2-Shorts")
    @PostMapping("/Reply")
    public Resp reply(@Validated ReplyDTO replyDTO) {
        boolean exists = false;
        if (replyDTO.getToContextType() == 0) {
            exists = userService.lambdaQuery().eq(User::getUserId, replyDTO.getToContextId()).exists();
        } else if (replyDTO.getToContextType() == 2) {
            exists = shortsService.lambdaQuery().eq(Shorts::getId, replyDTO.getToContextId()).exists();
        }
        if (!exists) return error("回复失败");

        Replys replys = new Replys();
        replys.setUserId(LoginUserHolder.get(User.class).getUserId());
        replys.setToContextType(replyDTO.getToContextType());
        replys.setToContextId(replyDTO.getToContextId());
        replys.setReplyContext(replyDTO.getReplyContext());
        replysService.save(replys);


        shortsService.lambdaUpdate()
                .eq(Shorts::getId, getContextId(replyDTO.getToContextType(), replyDTO.getToContextId()))
                .setSql("replys=replys+1")
                .update();

        return success();
    }

    //    若是回复用户，就向上查，直到找到所在的文章id，若是回复文章的就直接返回
    Integer getContextId(Integer type, Integer contextId) {
        if (type == 0) {
            Replys replys = replysService.lambdaQuery()
                    .select(Replys::getId, Replys::getToContextType)
                    .eq(Replys::getId, contextId)
                    .one();
            if (replys.getToContextType() != 0) return replys.getId();
            getContextId(replys.getToContextType(), replys.getId());
        }
        return contextId;
    }

}

