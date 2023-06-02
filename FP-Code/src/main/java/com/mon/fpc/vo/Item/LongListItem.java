package com.mon.fpc.vo.Item;

import com.mon.fpc.entity.Longs;
import lombok.Data;

/**
 * @author clic
 * @date 2023-05-16 21:33
 * @description:
 */
@Data
public class LongListItem extends Longs {

    private String username;

    private String tags;

    private String[] tagsList;

}
