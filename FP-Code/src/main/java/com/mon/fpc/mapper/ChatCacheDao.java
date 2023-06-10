package com.mon.fpc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mon.fpc.entity.ChatCache;
import com.mon.fpc.vo.ChatMessageVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author clic
 * @date 2023-06-10 14:26
 * @description:
 */

@Mapper
public interface ChatCacheDao extends BaseMapper<ChatCache> {

    @Insert("<script>" +
            " insert into chat_cache(sender_id,receiver_id,content_type,message_content) values" +
            " <foreach collection=\"list\" index=\"index\" item=\"item\" separator=\",\">\n" +
            " ( #{item.senderId}, #{item.receiverId}, #{item.contentType}, #{item.message} )" +
            " </foreach>" +
            "</script>")
    Integer batchInsert(@Param("list") List<ChatMessageVo> list);

}

