package com.kcunha.spring_boot_react_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@SpringBootTest(
    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.compatibility-verifier.enabled=false",
        "spring.main.web-application-type=servlet"
    }
)
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {
    org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration.class,
    org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration.class,
    org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration.class
})
public class SpringBootReactAppApplicationTests {

    @Test
    void contextLoads() {
    }

}
