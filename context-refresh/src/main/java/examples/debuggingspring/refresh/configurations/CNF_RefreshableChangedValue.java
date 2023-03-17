package examples.debuggingspring.refresh.configurations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
@ConditionalOnProperty(name = "prop.for.refresh", havingValue = "changed-value")
public class CNF_RefreshableChangedValue {

    @Bean(name = "CDB_RefreshableConfigChangedValue")
    public ConfigDefinedBean refreshableConfigDefinedBean() {
        return new ConfigDefinedBean();
    }

}
