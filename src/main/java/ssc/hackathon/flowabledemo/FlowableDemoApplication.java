package ssc.hackathon.flowabledemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ssc.hackathon.flowabledemo.service.MyService;

@SpringBootApplication
public class FlowableDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowableDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(final MyService myService) {

        return strings -> myService.createDemoUsers();
    }
}
