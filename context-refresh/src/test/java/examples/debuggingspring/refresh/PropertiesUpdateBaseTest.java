package examples.debuggingspring.refresh;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class PropertiesUpdateBaseTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;
    @Autowired
    protected ApplicationContext context;

    @Autowired
    protected ConfigurableListableBeanFactory clbf;

    protected void callActuatorUrl(String url, HttpEntity<Object> request) {
        ResponseEntity<String> response = testRestTemplate.exchange(
                url, HttpMethod.POST, request, String.class
        );
        assertSuccess(response);
    }

    protected HttpEntity<Object> changePropRequest() {
        return new HttpEntity<>(
                "{" +
                        "\"name\":\"prop.for.refresh\", " +
                        "\"value\":\"changed-value\"" +
                        "}",
                jsonHeaders()
        );
    }

    protected HttpEntity<Object> refreshRequest() {
        return new HttpEntity<>(jsonHeaders());
    }

    protected HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        return headers;
    }

    private void assertSuccess(ResponseEntity<?> response) {
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

}
