package org.example.authtesterapi;

import org.example.authtesterapi.Service.LinkGenerateService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AuthTesterApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AuthTesterApiApplication.class, args);
        LinkGenerateService linkGenerateService = context.getBean(LinkGenerateService.class);
    }

}
