package examples.debuggingtenant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@AutoConfigureMockMvc
public class TenantScopedDebugTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void byDefaultWeWillOnlyGetInfoLogs(CapturedOutput output) throws Exception {
        mockMvc.perform(get("/api/tenants/some-tenant/action"))
                .andExpect(status().isOk());
        String out = output.getOut();
        assertThat(out).contains("Info log that we got request from tenant some-tenant");
        assertThat(out).doesNotContain("And here is some debug data for this request");
    }

    @Test
    void enableTenantDebug_willProduceDebug(CapturedOutput output) throws Exception {
        mockMvc.perform(post("/actuator/env").contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                "name": "tenants-for-debugging",
                                "value": "32167"
                                }
                                """
                )).andExpect(status().isOk());

        mockMvc.perform(get("/api/tenants/32167/action"))
                .andExpect(status().isOk());
        String out = output.getOut();
        assertThat(out).contains("Info log that we got request from tenant 32167");
        assertThat(out).contains("And here is some debug data for tenant 32167");
    }

}
