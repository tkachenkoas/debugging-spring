package examples.debuggingspring.refresh.beans;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@RefreshScope
public class RefreshScopeResolverBasedBean extends IdAndPropAware {

    private final PropertyResolver propertyResolver;

    public String getProperty() {
        return propertyResolver.getProperty("prop.for.refresh");
    }


}
