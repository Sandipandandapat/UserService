package com.proj.user.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyConfig {
    @Bean                 //if we don't configure this, resttemplate will still work
    @LoadBalanced  //when we use it with http requests spring will automatically distribute the requests among multiple instances of that service
    //if we use it we can directly use the service name instead of hard coded url with hostname and port
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
