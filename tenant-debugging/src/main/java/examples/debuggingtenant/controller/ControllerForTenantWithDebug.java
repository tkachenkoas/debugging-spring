package examples.debuggingtenant.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static examples.debuggingtenant.TenantDebuggingHandlerInterceptor.TENANT_ID_TEMPLATE_VAR;

@RestController
@Slf4j
public class ControllerForTenantWithDebug {
    @GetMapping("/api/tenants/{" + TENANT_ID_TEMPLATE_VAR + "}/action")
    public void doLogSomething(
            @PathVariable(TENANT_ID_TEMPLATE_VAR) String tenantId
    ) {
        log.info("Info log that we got request from tenant {}", tenantId);
        log.debug("And here is some debug data for tenant {}", tenantId);
    }

}
