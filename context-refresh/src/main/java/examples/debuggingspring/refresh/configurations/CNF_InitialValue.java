package examples.debuggingspring.refresh.configurations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "prop.for.refresh", havingValue = "initial-value")
public class CNF_InitialValue {

    @Bean(name = "CDB_ConfigInitialValue")
    public ConfigDefinedBean fromConfigForInitialValue() {
        return new ConfigDefinedBean();
    }

}
