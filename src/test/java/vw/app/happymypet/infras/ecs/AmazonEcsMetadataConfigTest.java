package vw.app.happymypet.infras.ecs;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AmazonEcsMetadataConfigTest {

    @Autowired
    AmazonEcsMetadata amazonEcsMetadata;

    @Autowired
    CloudWatchMetricsPublisher cloudWatchMetricsPublisher;

    @BeforeClass
    public static void setup() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("vw/demo/petclinic/infras/ecs/ecs_container_metadata_exam.json");
        System.out.println("### ECS_CONTAINER_METADATA_FILE example file path : " + classPathResource.getURL().getPath());
        System.setProperty("ECS_CONTAINER_METADATA_FILE", classPathResource.getURL().getPath());
    }

    @Test
    @Ignore
    public void isAmazonEcsMetadataExist() {
        log.info(amazonEcsMetadata.toString());
        assertThat(amazonEcsMetadata.isExist(), is(true));
    }
}