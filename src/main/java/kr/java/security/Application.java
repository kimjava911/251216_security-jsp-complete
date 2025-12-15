package kr.java.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Audit (createdAt)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
