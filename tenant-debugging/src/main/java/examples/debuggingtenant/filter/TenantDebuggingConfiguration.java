package examples.debuggingtenant.filter;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.turbo.MDCFilter;
import examples.debuggingtenant.TenantDebuggingHandlerInterceptor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TenantDebuggingConfiguration {
    public static final String TENANT_DEBUGGING_MDC_KEY = "tenant-log-level";
    public static final String TENANT_DEBUG_LEVEL = "enabled";

    @Autowired
    public TenantDebuggingConfiguration() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        MDCFilter filter = new TenantDebugMdcFilter();
        filter.setMDCKey(TENANT_DEBUGGING_MDC_KEY);
        filter.setValue(TENANT_DEBUG_LEVEL);
        filter.setOnMatch("ACCEPT");

        loggerContext.addTurboFilter(filter);
    }

    @Bean
    public WebMvcConfigurer interceptorsConfigurer(TenantDebuggingHandlerInterceptor debuggingHandlerInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(debuggingHandlerInterceptor);
            }
        };
    }
}
