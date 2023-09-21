package ssc.hackathon.flowabledemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import ssc.hackathon.flowabledemo.conf.AppDispatcherServletConfiguration;
import ssc.hackathon.flowabledemo.conf.ApplicationConfiguration;
import ssc.hackathon.flowabledemo.conf.DatabaseAutoConfiguration;
import ssc.hackathon.flowabledemo.service.MyService;


@Import(value={

        // 引入修改的配置
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class,
        // 引入 DatabaseConfiguration 表更新转换
        DatabaseAutoConfiguration.class})
// Eureka 客户端
//@EnableDiscoveryClient
@ComponentScan(basePackages = {
        "ssc.hackathon.flowabledemo.*"})
@MapperScan("ssc.hackathon.flowabledemo.*.dao")
@SpringBootApplication(exclude={org.flowable.ui.modeler.conf.ApplicationConfiguration.class,
        SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
public class FlowableDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowableDemoApplication.class, args);
    }

}
