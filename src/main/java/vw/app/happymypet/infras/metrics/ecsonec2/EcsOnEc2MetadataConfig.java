package vw.app.happymypet.infras.metrics.ecsonec2;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class EcsOnEc2MetadataConfig {
    @Value("${ECS_CONTAINER_METADATA_FILE:}")
    private String ecsContainerMetadataFilePath;

    private ObjectMapper mapper = getObjectMapper();

    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public EcsOnEc2Metadata amazonEcsMetadata() {
        if(StringUtils.isEmpty(ecsContainerMetadataFilePath)) {
            log.warn("There is no $ECS_CONTAINER_METADATA_FILE.");
            return new EcsOnEc2Metadata(false);
        }
        try {
            String metaFileContent = Files.readAllLines(Paths.get(ecsContainerMetadataFilePath)).stream()
                    .collect(Collectors.joining("\n"));
            log.info("### ECS_CONTAINER_METADATA_FILE Contents");
            log.info(metaFileContent);
            return mapper.readValue(metaFileContent, EcsOnEc2Metadata.class);
        } catch (IOException e){
            log.warn("There is no ECS container meta file.", e);
            return new EcsOnEc2Metadata(false);
        }
    }
}
