package com.mon.fpc.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author clic
 * @date 2023-05-10 14:40
 * @description:
 */
@Component
public class ImgUtil {

//    将图片字符串按照 空格 切开并加上前缀
    public String[] initImgs(String imgs){
         return Arrays.stream(imgs.split(" "))
                .map(img->"http://127.0.0.1/imgs/"+img)
                .toArray(String[]::new);

    }

}
