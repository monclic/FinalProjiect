package com.mon.fpc.controller;


import com.mon.fpc.Service.BoardService;
import com.mon.fpc.core.BaseController;
import com.mon.fpc.core.Resp;
import com.mon.fpc.dto.AdminLoginDTO;
import com.mon.fpc.entity.Admins;
import com.mon.fpc.entity.Board;
import com.mon.fpc.utils.LoginUserHolder;
import com.mon.fpc.vo.CommonListVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author clic
 * @since 2023-05-23 01:16:05
 */
@RestController
@RequestMapping("/board")
public class BoardController extends BaseController {

    @Resource
    private BoardService boardService;

    @ApiOperation(value = "")
    @GetMapping("/list")
    private Resp list() {
        List<Board> list = boardService.lambdaQuery()
                .list();

        CommonListVO<Board> boardCommonListVO = new CommonListVO<>();

        boardCommonListVO.setList(list);
        return success(boardCommonListVO);
    }

    @ApiOperation(value = "")
    @PostMapping("/delete")
    private Resp delete(@RequestBody String id) {
        boardService.removeById(id);
        return success();
    }


    @ApiOperation(value = "")
    @PostMapping("/add")
    private Resp add(@RequestBody String textarea) {

        Board board = new Board();
        board.setAdminId(111);
        board.setText(textarea);

        boardService.save(board);

        return success();
    }
}

