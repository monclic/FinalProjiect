package com.mon.fpc.vo.Item;

import com.mon.fpc.entity.Shorts;
import lombok.Data;

/**
 * @author clic
 * @date 2023-05-09 16:32
 * @description:
 */
@Data
public class ShortListItem extends Shorts {
//    TODO
    private String username="默认名";
//    是否存在图片
    private boolean imagesIs=false;

    private String[] imagesList;


    private boolean likeIs=false;
}
