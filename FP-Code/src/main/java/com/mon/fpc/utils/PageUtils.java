package com.mon.fpc.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author clic
 * @date 2023-05-09 17:07
 * @description:
 */

public class PageUtils {
        /**
         * 判断是否有下一页
         */
        public static boolean isNextPage(Page<?> pageInfo) {
            long current = pageInfo.getCurrent() * pageInfo.getSize();
            return current < pageInfo.getTotal();
        }
}
