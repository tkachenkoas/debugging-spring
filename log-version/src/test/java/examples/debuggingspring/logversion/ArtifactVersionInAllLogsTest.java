package examples.debuggingspring.logversion;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@Slf4j
public class ArtifactVersionInAllLogsTest {

    @Test
    void allLogsShouldHaveBuildVersion(CapturedOutput output) {
        log.info("Any random log");
        String out = output.getOut();
        assertThat(out).contains("-v latest");
    }

}
