package com.mon.fpc.service.impl;

import com.mon.fpc.entity.Likes;
import com.mon.fpc.mapper.LikesMapper;
import com.mon.fpc.service.LikesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 点赞相关 服务实现类
 * </p>
 *
 * @author clic
 * @since 2023-05-03 04:03:33
 */
@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes> implements LikesService {

}
