package com.mon.fpc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mon.fpc.Service.LongsService;
import com.mon.fpc.Service.TagsService;
import com.mon.fpc.Service.UserService;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.dto.LongsPublishDTO;
import com.mon.fpc.dto.ShortPublishDTO;
import com.mon.fpc.dto.UpdateLongDTO;
import com.mon.fpc.entity.Likes;
import com.mon.fpc.entity.Longs;
import com.mon.fpc.entity.Shorts;
import com.mon.fpc.entity.Tags;
import com.mon.fpc.mapper.LongsMapper;
import com.mon.fpc.utils.ImgUtil;
import com.mon.fpc.utils.LoginUserHolder;
import com.mon.fpc.utils.PageUtils;
import com.mon.fpc.vo.*;
import com.mon.fpc.vo.Item.LongListItem;
import com.mon.fpc.vo.Item.ShortListItem;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author clic
 * @since 2023-05-16 09:20:37
 */
@RestController
@RequestMapping("/longs")
public class LongsController extends BaseController {

    @Resource
    private LongsService longsService;
    @Resource
    private LongsMapper longsMapper;
    @Resource
    private UserService userService;
    @Resource
    private com.mon.fpc.service.LikesService likesService;
    @Resource
    private TagsService tagsService;


    @ApiOperation(value = "Shorts首页列表获取")
    @GetMapping("/LongList")
    public Resp list(String PageNumber, String PageSize) {
        Page<Shorts> pageInfo = new Page<>(Long.parseLong(PageNumber), Long.parseLong(PageSize));


        QueryWrapper<Shorts> queryWrapper = new QueryWrapper<>();

        List<LongListItem> list = longsMapper.getList(pageInfo, queryWrapper);

        for (LongListItem i:list){
            if (!i.getTags().equals("")) {

                String[] s = Arrays.stream(i.getTags().split(" "))
                        .toArray(String[]::new);

                i.setTagsList(s);
            }
        }

        LongsListVO longsListVO = new LongsListVO();
        longsListVO.setList(list);
        longsListVO.setNextPageIs(PageUtils.isNextPage(pageInfo));
        return success(longsListVO);
    }

    @ApiOperation(value = "Longs发布或存草稿")
    @PostMapping("/LongPublish")
    public Resp publish(@RequestBody LongsPublishDTO longsPublishDTO) {
        com.entity.User user = LoginUserHolder.get(com.entity.User.class);

        Longs longs = new Longs();

        longs.setUserId(user.getUserId());
        longs.setText(longsPublishDTO.getText());
        longs.setArticleStatus(longsPublishDTO.getType());
        longs.setArticleStatus(1);
        if (!longsPublishDTO.getTags().equals(""))
            longs.setTags((longsPublishDTO.getTags()));

        longsService.save(longs);

        if (!longsPublishDTO.getTags().equals("")){
            String[] list = Arrays.stream(longs.getTags().split(" "))
                    .toArray(String[]::new);

            for (String s:list){
                boolean exists = tagsService.lambdaQuery()
                        .eq(Tags::getTagName, s)
                        .exists();
                if (!exists){
                    Tags tags = new Tags();
                    tags.setTagName(s);
                    tags.setLongsSum(1);
                    tagsService.save(tags);
                }else {
                    tagsService.lambdaUpdate()
                            .setSql("longs_sum=longs_sum+1")
                            .eq(Tags::getTagName,s)
                            .update();
                }
            }
        }
        return success();
    }

    //    查看
    @ApiOperation(value = "LongDetail查看")
    @GetMapping("/Long")
    public Resp getLongs(@RequestParam String longId) {
        Longs longs = longsService.lambdaQuery()
                .eq(Longs::getId, longId)
                .one();
        LongDetailVO longDetailVO = new LongDetailVO();
        BeanUtils.copyProperties(longs, longDetailVO);

        com.entity.User user = userService.lambdaQuery()
                .select(com.entity.User::getUserNickName, com.entity.User::getUserAvatar)
                .eq(com.entity.User::getUserId, longs.getUserId())
                .one();

        longDetailVO.setUsername(user.getUserNickName());
        longDetailVO.setAvatar(new ImgUtil().initImgs(user.getUserAvatar())[0]);

        if (!longs.getTags().equals("")) {

            String[] s = Arrays.stream(longs.getTags().split(" "))
                    .toArray(String[]::new);

            longDetailVO.setTags(s);
        }
        boolean exists = likesService.lambdaQuery()
                .eq(Likes::getUserId, LoginUserHolder.get(com.entity.User.class).getUserId())
                .eq(Likes::getContextId, longs.getId())
                .eq(Likes::getContextType, 1)
                .exists();
        longDetailVO.setLikeIs(exists);

        return success(longDetailVO);
    }

    @ApiOperation(value = "获取个人的long列表")
    @GetMapping("/PersonalLongs")
    public Resp PersonalLongs(String PageNumber, String PageSize){
        Integer uid = LoginUserHolder.get(com.entity.User.class).getUserId();

        Page<Shorts> pageInfo = new Page<>(Long.parseLong(PageNumber), Long.parseLong(PageSize));


        List<LongListItem> list=longsMapper.PersonalLongs(pageInfo,uid);


        for (LongListItem i:list){
            if (!i.getTags().equals("")) {

                String[] s = Arrays.stream(i.getTags().split(" "))
                        .toArray(String[]::new);

                i.setTagsList(s);
            }
        }


        LongsListVO longsListVO = new LongsListVO();
        longsListVO.setList(list);
        longsListVO.setNextPageIs(PageUtils.isNextPage(pageInfo));
        return success(longsListVO);
    }

    @ApiOperation(value = "Long删除")
    @PostMapping("/delete")
    public Resp delete(@RequestBody String sid) {
        Integer userId = LoginUserHolder.get(com.entity.User.class).getUserId();
        boolean exists = longsService.lambdaQuery()
                .eq(Longs::getUserId, userId)
                .eq(Longs::getId, sid)
                .exists();
        if (!exists) return error("删除失败");

        longsService.removeById(sid);
        return success();
    }

    @ApiOperation(value = "tag->longs")
    @GetMapping("/tag_long")
    public Resp tag_long(String  tag,String PageNumber, String PageSize) {
        Page<Shorts> pageInfo = new Page<>(Long.parseLong(PageNumber), Long.parseLong(PageSize));


        QueryWrapper<Shorts> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("and s.tags like '%"+tag+"%'");

        List<LongListItem> list = longsMapper.getList(pageInfo, queryWrapper);

        for (LongListItem i:list){
            if (!i.getTags().equals("")) {

                String[] s = Arrays.stream(i.getTags().split(" "))
                        .toArray(String[]::new);

                i.setTagsList(s);
            }
        }

        LongsListVO longsListVO = new LongsListVO();
        longsListVO.setList(list);
        longsListVO.setNextPageIs(PageUtils.isNextPage(pageInfo));
        return success(longsListVO);
    }

    @ApiOperation(value = "获取个人的long草稿列表")
    @GetMapping("/PersonalLongsGraft")
    public Resp PersonalLongsGraft(String PageNumber, String PageSize){
        Integer uid = LoginUserHolder.get(com.entity.User.class).getUserId();

        Page<Shorts> pageInfo = new Page<>(Long.parseLong(PageNumber), Long.parseLong(PageSize));


        List<LongListItem> list=longsMapper.PersonalLongsGraft(pageInfo,uid);


        for (LongListItem i:list){
            if (!i.getTags().equals("")) {

                String[] s = Arrays.stream(i.getTags().split(" "))
                        .toArray(String[]::new);

                i.setTagsList(s);
            }
        }


        LongsListVO longsListVO = new LongsListVO();
        longsListVO.setList(list);
        longsListVO.setNextPageIs(PageUtils.isNextPage(pageInfo));
        return success(longsListVO);
    }

    @ApiOperation(value = "更新longs")
    @PostMapping("/update")
    public Resp update(@RequestBody UpdateLongDTO updateLongDTO){
        longsService.lambdaUpdate()
                .set(Longs::getText,updateLongDTO.getText())
                .set(Longs::getTags,updateLongDTO.getTags())
                .set(Longs::getArticleStatus,updateLongDTO.getType())
                .eq(Longs::getId,updateLongDTO.getLid())
                .update();
        return success();
    }

    @ApiOperation(value = "Long删除")
    @PostMapping("/deletea")
    public Resp deletea(@RequestBody String sid) {
        boolean exists = longsService.lambdaQuery()
                .eq(Longs::getId, sid)
                .exists();
        if (!exists) return error("删除失败");

        longsService.removeById(sid);
        return success();
    }

}

