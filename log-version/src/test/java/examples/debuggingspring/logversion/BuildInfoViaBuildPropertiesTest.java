package examples.debuggingspring.logversion;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@Slf4j
public class BuildInfoViaBuildPropertiesTest {

    @Autowired
    private BuildProperties buildProperties;

    @Test
    void buildPropertiesShouldBeAvailable(CapturedOutput output) {
        log.info("Build version is {}", buildProperties.getVersion());
        String out = output.getOut();
        assertThat(out).contains("Build version is 1.0.0");
    }

}
