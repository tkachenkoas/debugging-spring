package examples.debuggingtenant;

import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

import static examples.debuggingtenant.filter.TenantDebuggingConfiguration.TENANT_DEBUGGING_MDC_KEY;
import static examples.debuggingtenant.filter.TenantDebuggingConfiguration.TENANT_DEBUG_LEVEL;

@Component
@RequiredArgsConstructor
public class TenantDebuggingHandlerInterceptor implements HandlerInterceptor {

    public static final String TENANT_ID_TEMPLATE_VAR = "tenant-id";

    private final PropertyResolver propertyResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Set tenantsForDebugging = propertyResolver.getProperty(
                "tenants-for-debugging", Set.class, Set.of()
        );
        if (tenantsForDebugging.isEmpty()) {
            return true;
        }
        Map<String, String> templateVariables = (Map<String, String>) request.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE
        );
        if (ObjectUtils.isEmpty(templateVariables)) {
            return true;
        }
        if (!templateVariables.containsKey(TENANT_ID_TEMPLATE_VAR)) {
            return true;
        }
        String pathVarValue = templateVariables.get(TENANT_ID_TEMPLATE_VAR);
        if (tenantsForDebugging.contains(pathVarValue)) {
            MDC.put(TENANT_DEBUGGING_MDC_KEY, TENANT_DEBUG_LEVEL);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        MDC.remove(TENANT_DEBUGGING_MDC_KEY);
    }

}
