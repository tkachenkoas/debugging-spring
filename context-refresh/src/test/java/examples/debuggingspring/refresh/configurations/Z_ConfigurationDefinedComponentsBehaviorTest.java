package examples.debuggingspring.refresh.configurations;

import examples.debuggingspring.refresh.PropertiesUpdateBaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils.qualifiedBeanOfType;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Z_ConfigurationDefinedComponentsBehaviorTest extends PropertiesUpdateBaseTest {

    @Order(value = 0)
    @Test
    void configurations_areContextComponents() {
        assertThat(context.getBean(CNF_NonConditional.class)).isNotNull();
        assertThat(context.getBean(CNF_InitialValue.class)).isNotNull();

        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
                .isThrownBy(() -> context.getBean(CNF_RefreshableChangedValue.class));
    }

    @Order(value = 1)
    @Test
    void beansFromCreatedConfigs_areAvailableInContext() {
        assertThat(context.getBeansOfType(ConfigDefinedBean.class)).hasSize(2);
        assertThat(qualifiedBeanOfType(
                context, ConfigDefinedBean.class, "CDB_ConfigInitialValue"
        )).isNotNull();

        assertThat(qualifiedBeanOfType(
                context, ConfigDefinedBean.class, "CDB_InitialValue"
        )).isNotNull();
    }

    @Order(value = 2)
    @Test
    void updateProperty_sameBeansAndConfigs() {
        callActuatorUrl("/actuator/env", changePropRequest());

        assertThat(context.getBean(CNF_NonConditional.class)).isNotNull();
        assertThat(context.getBean(CNF_InitialValue.class)).isNotNull();

        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
                .isThrownBy(() -> context.getBean(CNF_RefreshableChangedValue.class))
                .withMessageContaining(
                        "No qualifying bean of type 'examples.debuggingspring.refresh.configurations.CNF_RefreshableChangedValue' available"
                );

        assertThat(context.getBeansOfType(ConfigDefinedBean.class)).hasSize(2);
    }

    @Order(value = 3)
    @Test
    void refreshContext_newConfigsAndBeans_willNotAppear() {
        callActuatorUrl("/actuator/refresh", refreshRequest());

        assertThat(context.getBean(CNF_NonConditional.class)).isNotNull();
        assertThat(context.getBean(CNF_InitialValue.class)).isNotNull();

        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
                .isThrownBy(() -> context.getBean(CNF_RefreshableChangedValue.class))
                .withMessageContaining(
                        "No qualifying bean of type 'examples.debuggingspring.refresh.configurations.CNF_RefreshableChangedValue' available"
                );
        assertThat(context.getBeansOfType(ConfigDefinedBean.class)).hasSize(2);
    }

    @Order(value = 4)
    @Test
    void beansAreCreated_FromBeanDefinitions() {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();

        List<String> configs = Arrays.stream(beanDefinitionNames).filter(val -> val.startsWith("CNF"))
                .collect(Collectors.toList());
        assertThat(configs).hasSize(2)
                .contains("CNF_InitialValue", "CNF_NonConditional");
        List<String> beanDefs = Arrays.stream(beanDefinitionNames).filter(val -> val.startsWith("CDB"))
                .collect(Collectors.toList());
        assertThat(beanDefs).hasSize(2)
                .contains("CDB_ConfigInitialValue", "CDB_InitialValue");
    }

    @Order(value = 5)
    @Test
    void configurations_areSubClasses() {
        CNF_NonConditional config = context.getBean(CNF_NonConditional.class);
        assertThat(config).isInstanceOf(CNF_NonConditional.class);
        assertThat(config.getClass()).isNotEqualTo(CNF_NonConditional.class);
        assertThat(CNF_NonConditional.class).isAssignableFrom(config.getClass());
    }

    @Order(value = 6)
    @Test
    void configurations_areBeanFactoryBased() {
        CNF_NonConditional config = context.getBean(CNF_NonConditional.class);
        ConfigDefinedBean configDefinedBean = config.configDefinedForInitialValue();

        // same instance is returned
        assertThat(configDefinedBean).isSameAs(config.configDefinedForInitialValue());
        assertThat(configDefinedBean).isSameAs(config.configDefinedForInitialValue());

        // we can't fool Spring
        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
                .isThrownBy(config::configDefinedForChangedValue)
                .withMessageContaining("No bean named 'CDB_ChangedValue' available");
    }

}
