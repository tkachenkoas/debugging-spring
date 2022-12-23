package examples.debuggingspring.refresh.beans;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class SimpleRefreshScopedBean extends IdAndPropAware {

    @Override
    public String getProperty() {
        return "constant";
    }
}
