package me.lahirudilhara.webchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WebchatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebchatApplication.class, args);
    }

}
