package com.mon.fpc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.User;
import com.mon.fpc.Service.UserService;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.dto.ShortPublishDTO;
import com.mon.fpc.entity.Likes;
import com.mon.fpc.entity.Shorts;
import com.mon.fpc.mapper.ShortsMapper;
import com.mon.fpc.utils.ImgUtil;
import com.mon.fpc.utils.LoginUserHolder;
import com.mon.fpc.utils.PageUtils;
import com.mon.fpc.vo.CommonTypeVo;
import com.mon.fpc.vo.Item.ShortListItem;
import com.mon.fpc.vo.ShortDetailVO;
import com.mon.fpc.vo.ShortListVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author clic
 * @since 2023-05-03 11:07:24
 */
@RestController
@RequestMapping("/shorts")
public class ShortsController extends BaseController {

    @Resource
    private com.mon.fpc.service.ShortsService shortsService;
    @Resource
    private ShortsMapper shortsMapper;
    @Resource
    private UserService userService;
    @Resource
    private com.mon.fpc.service.LikesService likesService;

    //    查list
    @ApiOperation(value = "Shorts首页列表获取 order:1-综合 2-最新发布 3-最多点赞 4-最多回复")
    @GetMapping("/ShortList")
    public Resp list(String PageNumber, String PageSize,String order) {
        Page<Shorts> pageInfo = new Page<>(Long.parseLong(PageNumber), Long.parseLong(PageSize));


        QueryWrapper<Shorts> queryWrapper = new QueryWrapper<>();
        switch (order) {
            case "1":
                queryWrapper.orderByDesc("s.likes+s.replys");
                break;
            case "2":
                queryWrapper.orderByDesc("s.create_time");
                break;
            case "3":
                queryWrapper.orderByDesc("likes");
                break;
            case "4":
                queryWrapper.orderByDesc("replys");
                break;
        }

        List<ShortListItem> list=shortsMapper.getList(pageInfo,queryWrapper);
        list = list.stream().peek((item) -> {
            if (!item.getImages().equals("")) {
                item.setImagesIs(true);
                String[] images = new ImgUtil().initImgs(item.getImages());
                item.setImagesList(images);
                boolean exists = likesService.lambdaQuery()
                        .eq(Likes::getUserId, LoginUserHolder.get(User.class).getUserId())
                        .eq(Likes::getContextId, item.getId())
                        .eq(Likes::getContextType, 2)
                        .exists();
                item.setLikeIs(exists);
            }
        }).collect(Collectors.toList());
        ShortListVO shortListVO = new ShortListVO();
        shortListVO.setList(list);
        shortListVO.setNextPageIs(PageUtils.isNextPage(pageInfo));
        return success(shortListVO);
    }

    //    查看
    @ApiOperation(value = "ShortsDetail查看")
    @GetMapping("/Short")
    public Resp getShorts(@RequestParam String shortId) {
        Shorts shorts = shortsService.lambdaQuery()
                .eq(Shorts::getId, shortId)
                .eq(Shorts::getArticleStatus, 1)
                .one();
        ShortDetailVO shortDetailVO = new ShortDetailVO();
        BeanUtils.copyProperties(shorts,shortDetailVO);

        User user = userService.lambdaQuery()
                .select(User::getUserNickName, User::getUserAvatar)
                .eq(User::getUserId, shorts.getUserId())
                .one();

        shortDetailVO.setUsername(user.getUserNickName());
        shortDetailVO.setAvatar(new ImgUtil().initImgs(user.getUserAvatar())[0]);

        if(!shorts.getImages().equals("")){
            shortDetailVO.setImagesIs(true);

            String[] s = new ImgUtil().initImgs(shorts.getImages());

            shortDetailVO.setImagesList(s);

            boolean exists = likesService.lambdaQuery()
                    .eq(Likes::getUserId, LoginUserHolder.get(User.class).getUserId())
                    .eq(Likes::getContextId, shorts.getId())
                    .eq(Likes::getContextType, 2)
                    .exists();
            shortDetailVO.setLikeIs(exists);
        }

        return success(shortDetailVO);
    }

//    TODO 发布时加点动画
    //    发布
    @ApiOperation(value = "Shorts发布")
    @PostMapping("/ShortPublish")
    public Resp publish(@RequestBody ShortPublishDTO shortPublishDTO) {
        User user = LoginUserHolder.get(User.class);

        Shorts shorts = new Shorts();
        shorts.setUserId(user.getUserId());
        shorts.setShortContent(shortPublishDTO.getContext());
        shorts.setImages(shortPublishDTO.getImages());
        shorts.setArticleStatus(1);

        shortsService.save(shorts);
        return success();
    }






}

