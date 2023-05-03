package com.mon.fpc.vo;

import com.mon.fpc.core.Resp;
import lombok.Data;

/**
 * @author clic
 * @date 2023-05-03 15:37
 * @description:通用单元素返回类‘’
 */
@Data
public class CommonTypeVo<T> extends Resp {
    private T goods;
}
