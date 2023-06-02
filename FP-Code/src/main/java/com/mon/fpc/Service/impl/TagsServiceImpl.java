package com.mon.fpc.Service.impl;

import com.mon.fpc.Service.TagsService;
import com.mon.fpc.entity.Tags;
import com.mon.fpc.mapper.TagsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author clic
 * @since 2023-05-16 09:20:37
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService {

}
