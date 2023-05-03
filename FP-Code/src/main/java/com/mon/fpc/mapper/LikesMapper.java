package com.mon.fpc.mapper;

import com.mon.fpc.entity.Likes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 点赞相关 Mapper 接口
 * </p>
 *
 * @author clic
 * @since 2023-05-03 04:03:33
 */
@Mapper
public interface LikesMapper extends BaseMapper<Likes> {

}
