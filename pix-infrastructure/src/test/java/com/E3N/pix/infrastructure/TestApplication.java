package com.E3N.pix.infrastructure;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {
                "com.E3N.pix",
        }
)
@EntityScan(basePackages = "com.E3N.pix.infrastructure")
@EnableJpaRepositories(basePackages = "com.E3N.pix.infrastructure")
public class TestApplication {
}
