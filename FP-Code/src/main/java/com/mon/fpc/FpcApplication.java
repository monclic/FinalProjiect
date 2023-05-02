package com.mon.fpc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author clic
 * @date 2023-05-01 14:46
 * @description:
 */

@EnableSwagger2
@SpringBootApplication
@MapperScan(basePackages = {"com.mon.fpc.mapper","com.mon.fpc.config"})
@ServletComponentScan()
public class FpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(FpcApplication.class, args);
    }

}