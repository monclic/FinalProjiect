package com.mon.fpc.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.User;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.dto.ShortPublishDTO;
import com.mon.fpc.entity.Shorts;
import com.mon.fpc.mapper.ShortsMapper;
import com.mon.fpc.utils.LoginUserHolder;
import com.mon.fpc.vo.CommonListVO;
import com.mon.fpc.vo.CommonTypeVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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

    //    查list
    @ApiOperation(value = "Shorts首页列表获取")
    @GetMapping("/ShortList")
    public Resp list(String PageNumber, String PageSize) {
        Page<Shorts> pageInfo = new Page<>(Long.parseLong(PageNumber), Long.parseLong(PageSize));

        LambdaQueryWrapper<Shorts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Shorts::getLikes);
        Page<Shorts> shortsPage = shortsMapper.selectPage(pageInfo, queryWrapper);
        List<Shorts> list = shortsPage.getRecords();
        CommonListVO<Shorts> commonListVO = new CommonListVO<>();
        commonListVO.setList(list);
        return success(commonListVO);
    }

    //    查看
    @ApiOperation(value = "Shorts查看")
    @GetMapping("/Shorts")
    public Resp getShorts(String shortId) {
        Shorts shorts = shortsService.lambdaQuery()
                .eq(Shorts::getId, shortId)
                .eq(Shorts::getArticleStatus, 1)
                .one();

        CommonTypeVo<Shorts> commonTypeVo = new CommonTypeVo<>();
        commonTypeVo.setGoods(shorts);
        return commonTypeVo;
    }

    //    发布
    @ApiOperation(value = "Shorts发布")
    @PostMapping("/ShortPublish")
    public Resp publish(ShortPublishDTO shortPublishDTO) {
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

