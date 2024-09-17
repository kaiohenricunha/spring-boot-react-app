package com.kcunha.spring_boot_react_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringBootReactAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReactAppApplication.class, args);
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

    @Bean
    public OpenTelemetry openTelemetry() {
        return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
    }
}
