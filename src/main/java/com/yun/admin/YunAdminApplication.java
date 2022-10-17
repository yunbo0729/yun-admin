package com.yun.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

//@SpringBootApplication
//@Import(MybatisConfig.class)
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class YunAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(YunAdminApplication.class, args);
    }

}
