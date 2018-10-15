package vw.app.happymypet.infras.metrics;

import com.amazonaws.util.StringUtils;
import io.micrometer.cloudwatch.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class MetricsConfig {
    @Autowired
    Environment env;

    @Value("${HOSTNAME:}")
    String hostname;

    @Autowired
    CloudWatchMeterRegistry cloudWatchMeterRegistry;


    @PostConstruct
    void init() {
        String profile = Arrays.stream(env.getActiveProfiles()).collect(Collectors.joining(","));
        if (StringUtils.isNullOrEmpty(profile)) {
            profile = "default";
        }

        log.info("### PROFILE : " + profile);
        log.info("### HOSTNAME : " + hostname);


        if (StringUtils.isNullOrEmpty(hostname)) {
            cloudWatchMeterRegistry.config().commonTags(Collections.singletonList(Tag.of("profiles", profile)));
        } else {
            cloudWatchMeterRegistry.config().commonTags(Arrays.asList(Tag.of("host", hostname), Tag.of("profiles", profile)));
        }
        Metrics.addRegistry(cloudWatchMeterRegistry);
    }

}
