package examples.debuggingspring.refresh.beans;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@Getter
@RefreshScope
public class RefreshScopeInjectionBasedBean extends IdAndPropAware {

    private String property;

    @Autowired
    public void setProperty(@Value("${prop.for.refresh}") String property) {
        this.property = property;
    }
}
