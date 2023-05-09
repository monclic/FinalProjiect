package com.mon.fpc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.entity.User;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.dto.LikesDTO;
import com.mon.fpc.entity.Likes;
import com.mon.fpc.entity.Shorts;
import com.mon.fpc.utils.LoginUserHolder;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 点赞相关 前端控制器
 * </p>
 *
 * @author clic
 * @since 2023-05-03 04:03:33
 */
@RestController
@RequestMapping("/likes")
public class LikesController extends BaseController {
    @Resource
    private com.mon.fpc.service.ShortsService shortsService;
    @Resource
    private com.mon.fpc.service.LikesService likesService;

    @ApiOperation(value = "点赞Shorts 1-Longs 2-Shorts")
    @PostMapping("/setLike")
    public Resp setLike(@RequestBody LikesDTO likesDTO) {
        Integer type = likesDTO.getType();
        Integer contextId = likesDTO.getContextId();

        Integer userId = LoginUserHolder.get(User.class).getUserId();
        boolean contextExists = contextExist(type,contextId);
        if (!contextExists) return error("该内容不存在");

        boolean likeExists = likesService.lambdaQuery()
                .eq(Likes::getUserId, userId)
                .eq(Likes::getContextId, contextId)
                .exists();

//        若已点赞则直接返回
        if (likeExists) return success();

        Likes likes = new Likes();
        likes.setUserId(userId);
        likes.setContextType(type);
        likes.setContextId(contextId);
        likesService.save(likes);

        operationLikes("likes=likes+1",type,contextId);

        return success();
    }

    @ApiOperation(value = "取消点赞Shorts 1-Longs 2-Shorts")
    @PostMapping("/cancelLike")
    public Resp cancelLike(@RequestBody LikesDTO likesDTO) {
        Integer type = likesDTO.getType();
        Integer contextId = likesDTO.getContextId();

        boolean contextExist = contextExist(type, contextId);
        if (!contextExist) return success();

        Integer id = likesService.lambdaQuery()
                .select(Likes::getId)
                .eq(Likes::getUserId, LoginUserHolder.get(User.class).getUserId())
                .eq(Likes::getContextId, contextId)
                .eq(Likes::getContextType, type)
                .one().getId();
        likesService.removeById(id);

        operationLikes("likes=likes-1",type,contextId);

        return success();
    }

    boolean contextExist(Integer type,Integer contextId){
        if (type == 2) {
            return shortsService.lambdaQuery().eq(Shorts::getId, contextId).exists();
        }else {
            return false;
        }
    }

    void operationLikes(String sql,Integer type,Integer contextId){
        if (type == 2) {
            shortsService.lambdaUpdate()
                    .setSql(sql)
                    .eq(Shorts::getId,contextId)
                    .update();
        }
    }
}

