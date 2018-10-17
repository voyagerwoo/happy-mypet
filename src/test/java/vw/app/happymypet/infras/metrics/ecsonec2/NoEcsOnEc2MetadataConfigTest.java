package vw.app.happymypet.infras.metrics.ecsonec2;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vw.app.happymypet.infras.metrics.ecsonec2.EcsOnEc2Metadata;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class NoEcsOnEc2MetadataConfigTest {

    @Autowired
    EcsOnEc2Metadata ecsOnEc2Metadata;

    @BeforeClass
    public static void setup() throws IOException {
        System.setProperty("ECS_CONTAINER_METADATA_FILE", "");
    }

    @Test
    @Ignore
    public void isNotAmazonEcsMetadataExist() {
        log.info(ecsOnEc2Metadata.toString());
        assertThat(ecsOnEc2Metadata.isExist(), is(false));
    }
}