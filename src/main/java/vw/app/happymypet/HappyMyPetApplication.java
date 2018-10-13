package vw.app.happymypet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HappyMyPetApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappyMyPetApplication.class, args);
    }
}
