package com.mon.fpc.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mon.fpc.entity.Shorts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mon.fpc.vo.Item.ShortListItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author clic
 * @since 2023-05-03 11:07:24
 */
@Mapper
public interface ShortsMapper extends BaseMapper<Shorts> {
    List<ShortListItem> getList(Page<Shorts> pageInfo,@Param(Constants.WRAPPER) QueryWrapper<Shorts> queryWrapper);

    List<ShortListItem> getPersonalShorts(Page<Shorts> pageInfo,Integer uid);
}
