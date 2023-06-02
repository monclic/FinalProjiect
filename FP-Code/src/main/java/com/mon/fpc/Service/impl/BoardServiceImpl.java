package com.mon.fpc.Service.impl;

import com.mon.fpc.Service.BoardService;
import com.mon.fpc.entity.Board;
import com.mon.fpc.mapper.BoardMapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class BoardServiceImpl extends ServiceImpl<BoardMapper, Board> implements BoardService {

}
