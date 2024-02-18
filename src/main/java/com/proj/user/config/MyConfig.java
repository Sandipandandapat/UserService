package com.proj.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyConfig {
    @Bean                 //if we dont configure this, resttemplate will still work
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
