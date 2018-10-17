package vw.app.happymypet.infras.metrics;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vw.app.happymypet.infras.metrics.ecsonec2.EcsOnEc2Metadata;
import vw.app.happymypet.infras.metrics.fargate.FargateMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CloudWatchMetricsPublisher {
    @Value("${amazon.cloudwatch.metric.namespace:happy-mypet}")
    private String nameSpace;
    @Value("${cloud.aws.region.static:ap-northeast-1}")
    private String region;

    final private MetricsEndpoint metricsEndpoint;
    final private List<Dimension> dimensions;

    @Autowired
    public CloudWatchMetricsPublisher(MetricsEndpoint metricsEndpoint, EcsOnEc2Metadata ecsOnEc2Metadata, FargateMetadata fargateMetadata) {
        this.metricsEndpoint = metricsEndpoint;
        this.dimensions = getDimensions(ecsOnEc2Metadata, fargateMetadata);
    }

    @Scheduled(cron = "${spring.operational.metrics.cloudwatch.publish.cron:*/20 * * * * *}")
    public void publishMetrics() {
        if (dimensions.isEmpty())
            return;

        List<MetricDatum> datums = metricsEndpoint.listNames().getNames().stream()
                .map(name -> metricsEndpoint.metric(name, null))
                .map(metric -> metric.getMeasurements().stream().map(measurement -> new MetricDatum()
                        .withMetricName(metric.getName() + "." + measurement.getStatistic().toString())
                        .withDimensions(dimensions)
                        .withUnit(StandardUnit.None)
                        .withValue(measurement.getValue())))
                .flatMap(s -> s).collect(Collectors.toList());
        publish(datums);
    }

    private static List<Dimension> getDimensions(EcsOnEc2Metadata ecsOnEc2Metadata, FargateMetadata fargateMetadata) {
        List<Pair<String, String>> dimensions = new ArrayList<>();

        if (ecsOnEc2Metadata.isExist())
            dimensions.addAll(ecsOnEc2Metadata.dimensions());
        else if (fargateMetadata.isExist())
            dimensions.addAll(fargateMetadata.dimensions());

        return dimensions.stream()
                .map(p -> new Dimension().withName(p.getFirst()).withValue(p.getSecond()))
                .collect(Collectors.toList());
    }


    private void publish(List<MetricDatum> datums) {
        final int MAX_DATUM_SIZE = 20;
        List<List<MetricDatum>> datumGroups = new ArrayList<>();

        for(int i = 0, len = datums.size(); i<len ; i+=MAX_DATUM_SIZE) {
            int lastNum = i + MAX_DATUM_SIZE > len ? len : i + MAX_DATUM_SIZE;
            datumGroups.add(new ArrayList<>(datums.subList(i, lastNum)));
        }

        datumGroups.forEach(datumGroup -> {
            PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace(nameSpace)
                .withMetricData(datumGroup);
            PutMetricDataResult response = AmazonCloudWatchClientBuilder
                    .standard()
                    .withRegion(region)
                    .build().putMetricData(request);
            log.debug(response.toString());
        });
    }
}
