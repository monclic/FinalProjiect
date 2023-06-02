package com.mon.fpc.vo;

import com.mon.fpc.core.Resp;
import com.mon.fpc.vo.Item.LongListItem;
import com.mon.fpc.vo.Item.ShortListItem;
import lombok.Data;

import java.util.List;

/**
 * @author clic
 * @date 2023-05-16 21:35
 * @description:
 */
@Data
public class LongsListVO extends Resp {
    private List<LongListItem> list;
    private boolean nextPageIs;
}
