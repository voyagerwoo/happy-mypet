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
public class NoAmazonEcsMetadataConfigTest {

    @Autowired
    AmazonEcsMetadata amazonEcsMetadata;

    @BeforeClass
    public static void setup() throws IOException {
        System.setProperty("ECS_CONTAINER_METADATA_FILE", "");
    }

    @Test
    @Ignore
    public void isNotAmazonEcsMetadataExist() {
        log.info(amazonEcsMetadata.toString());
        assertThat(amazonEcsMetadata.isExist(), is(false));
    }
}