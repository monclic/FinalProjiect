package com.mon.fpc.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mon.fpc.entity.Longs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mon.fpc.entity.Shorts;
import com.mon.fpc.vo.Item.LongListItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author clic
 * @since 2023-05-16 09:20:37
 */
@Mapper
public interface LongsMapper extends BaseMapper<Longs> {

    List<LongListItem> getList(Page<Shorts> pageInfo, @Param(Constants.WRAPPER) QueryWrapper<Shorts> queryWrapper);

    List<LongListItem> PersonalLongs(Page<Shorts> pageInfo, Integer uid);

    List<LongListItem> PersonalLongsGraft(Page<Shorts> pageInfo, Integer uid);
}
