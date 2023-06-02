package com.mon.fpc.utils;

import com.mon.fpc.core.exception.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author clic
 * @date 2023-05-10 14:40
 * @description:
 */
@Component
public class ImgUtil {


    private static String imgUrl;

    @Value("${img_url}")
    public void setImgUrl(String imgUrl) {
        ImgUtil.imgUrl = imgUrl;
    }

//    将图片字符串按照 空格 切开并加上前缀
    public String[] initImgs(String imgs){
         return Arrays.stream(imgs.split(" "))
                .map(img->"http://127.0.0.1/imgs/"+img)
                .toArray(String[]::new);

    }
    /**
     * 保存图片
     */
    public static String saveImg(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new BaseException("请添加图片");
        }
        String name = file.getOriginalFilename();
        int index = name.indexOf(".");
        String suffix = name.substring(index);
        if (suffix.equalsIgnoreCase(".png")) {
            suffix = ".png";
        } else if (suffix.equalsIgnoreCase(".jpg")) {
            suffix = ".jpg";
        } else {
            throw new BaseException("请按照格式上传图片");
        }
        //防止文件名称重复造成文件覆盖
        String timeStamp = new SimpleDateFormat("yyyMMddHHmmss").format(new Date());
        String newName = name.substring(0, index) + "_" + timeStamp + suffix;

        String destFilePath = imgUrl + newName;

        //保存图片
        file.transferTo(new File(destFilePath));

        return newName;
    }

}
