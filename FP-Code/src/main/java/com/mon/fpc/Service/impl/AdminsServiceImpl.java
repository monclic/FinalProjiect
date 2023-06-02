package com.mon.fpc.Service.impl;

import com.mon.fpc.Service.AdminsService;
import com.mon.fpc.entity.Admins;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mon.fpc.mapper.AdminsMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author clic
 * @since 2023-05-23 01:16:05
 */
@Service
public class AdminsServiceImpl extends ServiceImpl<AdminsMapper, Admins> implements AdminsService {

}
