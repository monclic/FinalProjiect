package com.mon.fpc.controller;


import com.mon.fpc.Service.TagsService;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.entity.Tags;
import com.mon.fpc.vo.CommonListVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author clic
 * @since 2023-05-16 09:20:37
 */
@RestController
@RequestMapping("/tags")
public class TagsController extends BaseController {


    @Resource
    private TagsService tagsService;

    @ApiOperation(value = "获取热门tags")
    @GetMapping("/getTags")
    public Resp getTags(){
        List<Tags> list = tagsService.lambdaQuery()
                .orderByDesc(Tags::getLongsSum)
                .last("limit 9")
                .list();
        CommonListVO<Tags> tagsCommonListVO = new CommonListVO<>();
        tagsCommonListVO.setList(list);
        return success(tagsCommonListVO);
    }


}

