package examples.debuggingspring.logversion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class BuildVersionEnvironmentPostprocessor implements EnvironmentPostProcessor {

    /**
     * Will be added by spring-boot-gradle plugin into runtime classpath
     * <a href="https://docs.spring.io/spring-boot/docs/current/gradle-plugin/api/org/springframework/boot/gradle/tasks/buildinfo/BuildInfo.html">see docs</a>
     */
    private static final String BUILD_INFO_PROPS = "META-INF/build-info.properties";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            ClassPathResource buildProperties = new ClassPathResource(BUILD_INFO_PROPS);
            Properties props = PropertiesLoaderUtils.loadProperties(buildProperties);
            environment.getPropertySources()
                    .addLast(new PropertiesPropertySource(
                            "build-info", props
                    ));
        } catch (IOException ignore) {
            log.warn("Can't read properties from build-info file {}. Logs will not be enriched with build number",
                    BUILD_INFO_PROPS);
        }
    }
}
