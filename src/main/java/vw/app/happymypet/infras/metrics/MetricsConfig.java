package vw.app.happymypet.infras.metrics;

import io.micrometer.cloudwatch.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import vw.app.happymypet.infras.metrics.fargate.FargateMetadata;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class MetricsConfig {
    final private FargateMetadata fargateMetadata;

    final private CloudWatchMeterRegistry cloudWatchMeterRegistry;

    @Autowired
    public MetricsConfig(FargateMetadata fargateMetadata, CloudWatchMeterRegistry cloudWatchMeterRegistry) {
        this.fargateMetadata = fargateMetadata;
        this.cloudWatchMeterRegistry = cloudWatchMeterRegistry;
    }

    @PostConstruct
    void init() {
        log.info("### dimensions : " + fargateMetadata.dimensions());
        cloudWatchMeterRegistry.config().commonTags(fargateMetadata.dimensions().stream()
                        .map(p -> Tag.of(p.getFirst(), p.getSecond())).collect(Collectors.toList()));
        Metrics.addRegistry(cloudWatchMeterRegistry);
    }

}
