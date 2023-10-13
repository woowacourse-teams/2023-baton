package touch.baton.config.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.config.Order;
import org.slf4j.MDC;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.util.UUID;

@Order(Ordered.HIGHEST_PRECEDENCE)
class MDCLoggingFilter implements Filter {

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final String requestId = ((HttpServletRequest) servletRequest).getHeader("X-Request-ID");

        MDC.put("request_id", StringUtils.defaultString(requestId, UUID.randomUUID().toString()));
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }
}
