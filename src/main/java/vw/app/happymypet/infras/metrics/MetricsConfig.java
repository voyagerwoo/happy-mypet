package vw.app.happymypet.infras.metrics;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import vw.app.happymypet.infras.metrics.ecsonec2.EcsOnEc2Metadata;
import vw.app.happymypet.infras.metrics.fargate.FargateMetadata;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class MetricsConfig {
    final private FargateMetadata fargateMetadata;
    final private EcsOnEc2Metadata ecsOnEc2Metadata;

    @Autowired
    public MetricsConfig(FargateMetadata fargateMetadata, EcsOnEc2Metadata ecsOnEc2Metadata) {
        this.fargateMetadata = fargateMetadata;
        this.ecsOnEc2Metadata = ecsOnEc2Metadata;
    }

    private static final Duration HISTOGRAM_EXPIRY = Duration.ofMinutes(10);
    private static final Duration STEP = Duration.ofSeconds(5);

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        log.info("### dimensions : " + getDimensions());
        return registry -> registry.config()
                .commonTags(getDimensions().stream()
                    .map(p -> Tag.of(p.getFirst(), p.getSecond())).collect(Collectors.toList()))
                .meterFilter(MeterFilter.deny(id -> { // (4)
                    return id.getName().startsWith("hikaricp.");
                }))
                .meterFilter(new MeterFilter() {
                    @Override
                    public DistributionStatisticConfig configure(Meter.Id id,
                                                                 DistributionStatisticConfig config) {
                        return config.merge(DistributionStatisticConfig.builder()
                                .percentilesHistogram(true)
                                .percentiles(0.5, 0.75, 0.95) // (5)
                                .expiry(HISTOGRAM_EXPIRY) // (6)
                                .bufferLength((int) (HISTOGRAM_EXPIRY.toMillis() / STEP.toMillis())) // (7)
                                .build());
                    }
                });
    }

    private List<Pair<String, String>> getDimensions() {
        if (ecsOnEc2Metadata.isExist())
            return ecsOnEc2Metadata.dimensions();
        if (fargateMetadata.isExist())
            return fargateMetadata.dimensions();
        return Collections.emptyList();
    }

}
