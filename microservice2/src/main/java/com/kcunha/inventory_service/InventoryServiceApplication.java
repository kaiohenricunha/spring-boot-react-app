package com.kcunha.inventory_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

    // RestTemplate bean for non-test profiles
    @Bean
    @LoadBalanced
    @Profile("!test")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // RestTemplate bean for the test profile
    @Bean
    @Profile("test")
    public RestTemplate restTemplateForTests() {
        return new RestTemplate();
    }
}
