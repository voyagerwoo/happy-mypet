package vw.app.happymypet.infras.metrics.fargate;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
public class FargateMetadataConfigTest {

    private String fixture = "{\n" +
            "    \"Cluster\": \"arn:aws:ecs:ap-northeast-1:957582603404:cluster/happy-mypet\",\n" +
            "    \"TaskARN\": \"arn:aws:ecs:ap-northeast-1:957582603404:task/3ce35ace-b4f6-4d66-8d5a-caefb795db14\",\n" +
            "    \"Family\": \"happy-mypet-task\",\n" +
            "    \"Revision\": \"4\",\n" +
            "    \"DesiredStatus\": \"RUNNING\",\n" +
            "    \"KnownStatus\": \"RUNNING\",\n" +
            "    \"Containers\": [\n" +
            "        {\n" +
            "            \"DockerId\": \"52af4dc2aee96ad1edfedb8e573630d29895ce959248d22e2540a493d9c3c309\",\n" +
            "            \"Name\": \"~internal~ecs~pause\",\n" +
            "            \"DockerName\": \"ecs-happy-mypet-task-4-internalecspause-d4a1f190da8defc8a001\",\n" +
            "            \"Image\": \"fg-proxy:tinyproxy\",\n" +
            "            \"ImageID\": \"\",\n" +
            "            \"Labels\": {\n" +
            "                \"com.amazonaws.ecs.cluster\": \"arn:aws:ecs:ap-northeast-1:957582603404:cluster/happy-mypet\",\n" +
            "                \"com.amazonaws.ecs.container-name\": \"~internal~ecs~pause\",\n" +
            "                \"com.amazonaws.ecs.task-arn\": \"arn:aws:ecs:ap-northeast-1:957582603404:task/3ce35ace-b4f6-4d66-8d5a-caefb795db14\",\n" +
            "                \"com.amazonaws.ecs.task-definition-family\": \"happy-mypet-task\",\n" +
            "                \"com.amazonaws.ecs.task-definition-version\": \"4\"\n" +
            "            },\n" +
            "            \"DesiredStatus\": \"RESOURCES_PROVISIONED\",\n" +
            "            \"KnownStatus\": \"RESOURCES_PROVISIONED\",\n" +
            "            \"Limits\": {\n" +
            "                \"CPU\": 0,\n" +
            "                \"Memory\": 0\n" +
            "            },\n" +
            "            \"CreatedAt\": \"2018-10-16T15:28:53.279660278Z\",\n" +
            "            \"StartedAt\": \"2018-10-16T15:28:53.906742661Z\",\n" +
            "            \"Type\": \"CNI_PAUSE\",\n" +
            "            \"Networks\": [\n" +
            "                {\n" +
            "                    \"NetworkMode\": \"awsvpc\",\n" +
            "                    \"IPv4Addresses\": [\n" +
            "                        \"172.31.21.249\"\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"DockerId\": \"4ccfbe91eb5b08a23f0a5ed45bccca8a6622c98403a0577ea38b1453ca980567\",\n" +
            "            \"Name\": \"happy-mypet\",\n" +
            "            \"DockerName\": \"ecs-happy-mypet-task-4-happy-mypet-a691dcbc8bd1aae23600\",\n" +
            "            \"Image\": \"957582603404.dkr.ecr.ap-northeast-1.amazonaws.com/happy-mypet\",\n" +
            "            \"ImageID\": \"sha256:362f8efdaa14e31d9dacc2112a0d150d60aa60582ef62e7375cb362c6ed1aaa6\",\n" +
            "            \"Labels\": {\n" +
            "                \"com.amazonaws.ecs.cluster\": \"arn:aws:ecs:ap-northeast-1:957582603404:cluster/happy-mypet\",\n" +
            "                \"com.amazonaws.ecs.container-name\": \"happy-mypet\",\n" +
            "                \"com.amazonaws.ecs.task-arn\": \"arn:aws:ecs:ap-northeast-1:957582603404:task/3ce35ace-b4f6-4d66-8d5a-caefb795db14\",\n" +
            "                \"com.amazonaws.ecs.task-definition-family\": \"happy-mypet-task\",\n" +
            "                \"com.amazonaws.ecs.task-definition-version\": \"4\"\n" +
            "            },\n" +
            "            \"DesiredStatus\": \"RUNNING\",\n" +
            "            \"KnownStatus\": \"RUNNING\",\n" +
            "            \"Limits\": {\n" +
            "                \"CPU\": 0,\n" +
            "                \"Memory\": 0\n" +
            "            },\n" +
            "            \"CreatedAt\": \"2018-10-16T15:29:00.030218886Z\",\n" +
            "            \"StartedAt\": \"2018-10-16T15:29:00.752048167Z\",\n" +
            "            \"Type\": \"NORMAL\",\n" +
            "            \"Networks\": [\n" +
            "                {\n" +
            "                    \"NetworkMode\": \"awsvpc\",\n" +
            "                    \"IPv4Addresses\": [\n" +
            "                        \"172.31.21.249\"\n" +
            "                    ]\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ],\n" +
            "    \"Limits\": {\n" +
            "        \"CPU\": 0.5,\n" +
            "        \"Memory\": 1024\n" +
            "    },\n" +
            "    \"PullStartedAt\": \"2018-10-16T15:28:54.058794756Z\",\n" +
            "    \"PullStoppedAt\": \"2018-10-16T15:29:00.028049694Z\"\n" +
            "}";

    @Test
    public void parseMetadata() {
        FargateMetadata metadata = new FargateMetadataConfig().parseMetadata(fixture);
        log.info(metadata.toString());

        assertThat(metadata.isExist(), is(true));
        assertThat(metadata.getCluster(), is("arn:aws:ecs:ap-northeast-1:957582603404:cluster/happy-mypet"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseMetadata_without_content() {
        FargateMetadata metadata = new FargateMetadataConfig().parseMetadata("");
        log.info(metadata.toString());
    }
}