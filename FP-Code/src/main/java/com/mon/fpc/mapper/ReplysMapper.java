package com.mon.fpc.mapper;

import com.mon.fpc.entity.Replys;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mon.fpc.vo.Item.ReplyListItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 回复相关 Mapper 接口
 * </p>
 *
 * @author clic
 * @since 2023-05-03 04:03:33
 */
@Mapper
public interface ReplysMapper extends BaseMapper<Replys> {

    List<ReplyListItem> getChilds(@Param("toContextId") Integer id);
}
