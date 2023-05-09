package com.mon.fpc.vo;

import com.mon.fpc.core.Resp;
import com.mon.fpc.vo.Item.ShortListItem;
import lombok.Data;

import java.util.List;

/**
 * @author clic
 * @date 2023-05-09 17:08
 * @description:
 */
@Data
public class ShortListVO extends Resp {
private List<ShortListItem> list;
private boolean nextPageIs;
}
