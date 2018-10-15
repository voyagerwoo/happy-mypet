package vw.app.happymypet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class HappyMyPetApplication {

    public static void main(String[] args) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Map<String, String> envs = processBuilder.environment();
        envs.entrySet().forEach(entry -> log.info(entry.toString()));

        SpringApplication.run(HappyMyPetApplication.class, args);
    }
}
