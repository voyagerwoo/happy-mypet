package vw.app.happymypet.infras.metrics.ecsonec2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.util.Pair;
import vw.app.happymypet.infras.metrics.MetricsMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class EcsOnEc2Metadata implements MetricsMetadata {
    boolean isExist=true;
    String cluster;
    String containerInstanceARN;
    String taskARN;
    String taskDefinitionFamily;
    String taskDefinitionRevision;
    String containerID;
    String containerName;
    String dockerContainerName;
    String imageID;
    String imageName;
    List<PortMapping> portMappings;
    List<Network> networks;
    String metadataFileStatus;


    public EcsOnEc2Metadata(Boolean isExist) {
        this.isExist = isExist;
    }

    public String getTaskDefinition() {
        return taskDefinitionFamily + ":" + taskDefinitionRevision;
    }

    public String getContainerInstanceId() {
        String[] splited = containerInstanceARN.split("/");
        if (splited.length == 2)
            return splited[1];
        else
            return containerInstanceARN;
    }

    @Override
    public List<Pair<String, String>> dimensions() {
        if(!isExist)
            return Collections.emptyList();
        return new ArrayList<Pair<String, String>>(){{
            add(Pair.of("CLUSTER_NAME", cluster));
            add(Pair.of("CONTAINER_INSTANCE_ID", getContainerInstanceId()));
            add(Pair.of("TASK_DEFINITION_FAMILY", taskDefinitionFamily));
            add(Pair.of("CONTAINER_ID", containerID));
        }};
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class PortMapping {
        Integer containerPort;
        Integer hostPort;
        String bindIp;
        String protocol;
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