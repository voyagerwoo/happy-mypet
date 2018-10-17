package vw.app.happymypet.infras.metrics.fargate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class FargateMetadataConfig {
    private ObjectMapper mapper = getObjectMapper();

    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    FargateMetadata fargateMetadata() {
        try {
            Process process = Runtime.getRuntime().exec("curl -s --connect-timeout 5 http://169.254.170.2/v2/metadata/");
            String metadataContent = new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
                    .collect(Collectors.joining("\n"));
            int exitCode = process.waitFor();
            log.info("### METADATA BODY\n" + metadataContent);
            log.info("### exit code : " + exitCode);
            return parseMetadata(metadataContent);
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            log.warn("There is no fargate metadata.");
            return new FargateMetadata(false);
        }
    }


    FargateMetadata parseMetadata(String metadataContent) {
        try {
            return mapper.readValue(metadataContent, FargateMetadata.class);
        } catch (IOException e){
            throw new IllegalArgumentException();
        }
    }
}
