package org.example.springlogbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringLogBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringLogBackendApplication.class, args);
    }
}
