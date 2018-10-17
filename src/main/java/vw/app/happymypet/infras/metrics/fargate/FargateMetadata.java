package vw.app.happymypet.infras.metrics.fargate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import vw.app.happymypet.infras.metrics.MetricsMetadata;

import java.util.*;

@Getter
@NoArgsConstructor
@ToString
@Slf4j
public class FargateMetadata implements MetricsMetadata {
    boolean isExist = true;

    String cluster;
    String taskARN;
    String family;
    String revision;
    String desiredStatus;
    String knownStatus;
    List<Container> containers;
    Limits limits;
    Date pullStartedAt;
    Date PullStoppedAt;

    public FargateMetadata(boolean isExist) {
        this.isExist = isExist;
    }

    @Override
    public List<Pair<String, String>> dimensions() {
        if (!isExist)
            return Collections.emptyList();

        try {
            return new ArrayList<Pair<String, String>>() {{
                add(Pair.of("ClusterName", cluster.split("/")[1]));
                add(Pair.of("TaskId", taskARN.split("/")[1]));
                add(Pair.of("containerId", containers.stream()
                        .filter(c -> c.image.endsWith("happy-mypet"))
                        .findFirst().orElseThrow(IllegalArgumentException::new).dockerId));
            }};
        } catch(ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            log.error("Illegal fargate metadata", e);
            return Collections.emptyList();
        }
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Container {
        String dockerId;
        String name;
        String dockerName;
        String image;
        String imageID;
        Map<String, String> labels;
        String desiredStatus;
        String knownStatus;
        Limits limits;
        Date createdAt;
        Date startedAt;
        String type;
        List<Network> networks;
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Limits {
        double cpu;
        double memory;
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Network {
        String networkMode;
        @JsonProperty("IPv4Addresses")
        List<String> ipv4Addresses;
    }
}
