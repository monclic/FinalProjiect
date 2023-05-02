package com.mon.fpc.Service.impl;

import com.entity.User;

import com.mon.fpc.Service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mon.fpc.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author clic
 * @since 2023-05-02 02:42:51
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
