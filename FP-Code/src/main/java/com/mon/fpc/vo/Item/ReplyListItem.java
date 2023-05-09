package com.mon.fpc.vo.Item;

import com.mon.fpc.entity.Replys;
import lombok.Data;

import java.util.List;

/**
 * @author clic
 * @date 2023-05-09 21:11
 * @description:
 */
@Data
public class ReplyListItem extends Replys {
    private String username;
    private String avatar;
//    子回复是否存在
    private boolean childReplyIs = false;
    //    子回复
    private List<ReplyListItem> childList;
}
