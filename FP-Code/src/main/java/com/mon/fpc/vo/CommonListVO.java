package com.mon.fpc.vo;

import com.mon.fpc.core.Resp;
import lombok.Data;

import java.util.List;

/**
 * @author clic
 * @date 2023-05-03 15:16
 * @description:通用单list返回类
 */
@Data
public class CommonListVO<T> extends Resp {
    private List<T> list;
}
