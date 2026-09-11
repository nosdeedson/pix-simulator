package com.E3N.pix.soap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(
        scanBasePackages = {
                "com.E3N.pix",
        }
)
@EntityScan(basePackages = "com.E3N.pix.infrastructure")
@EnableJpaRepositories(basePackages = "com.E3N.pix.infrastructure")
public class PixSoapApplication {
    public static void main(String[] args) {
        System.out.println("... STARTING ...");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "test-integration");
        SpringApplication.run(PixSoapApplication.class, args);
    }
}
