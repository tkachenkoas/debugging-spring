package examples.debuggingspring.refresh.configurations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CNF_NonConditional {

    @ConditionalOnProperty(name = "prop.for.refresh", havingValue = "initial-value")
    @Bean(name = "CDB_InitialValue")
    public ConfigDefinedBean configDefinedForInitialValue() {
        return new ConfigDefinedBean();
    }

    @RefreshScope
    @ConditionalOnProperty(name = "prop.for.refresh", havingValue = "changed-value")
    @Bean(name = "CDB_ChangedValue")
    public ConfigDefinedBean configDefinedForChangedValue() {
        return new ConfigDefinedBean();
    }

}
