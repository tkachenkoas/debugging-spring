package examples.debuggingtenant.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.MDCFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

public class TenantDebugMdcFilter extends MDCFilter {

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t) {
        if (level != Level.DEBUG) {
            return FilterReply.NEUTRAL;
        }
        if (logger.getName().startsWith("examples.debuggingtenant")) {
            return super.decide(marker, logger, level, format, params, t);
        }
        return FilterReply.NEUTRAL;
    }
}
