package touch.baton.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Enumeration;

@Slf4j
public abstract class LoggerUtils {

    private LoggerUtils() {
    }

    public static void logError(final HttpServletRequest request, final Exception e) {
        final String headers = block(convertHeaders(request));
        log.error("""
                        {}
                        ========> exception : {}
                        ========> requestId : {}
                        ========> sessionId : {}
                        ========> remoteUser : {}
                        ========> remoteAddr : {}
                        ========> remoteHost : {}
                        ========> remotePort : {}
                        ========> http method : {}
                        ========> request url : {}
                        ========> character encoding : {}
                        ========> local : {}
                        ========> headers : {}
                        """,
                System.lineSeparator(),
                e,
                request.getRequestId(),
                request.getRequestedSessionId(),
                request.getRemoteUser(),
                request.getRemoteAddr(),
                request.getRemoteHost(),
                request.getRemotePort(),
                request.getMethod(),
                request.getRequestURL(),
                request.getCharacterEncoding(),
                request.getLocale(),
                headers
        );
    }

    public static void logWarn(final HttpServletRequest request, final RuntimeException e) {
        final String headers = block(convertHeaders(request));

        log.warn("""
                        {}
                        ========> exception : {}
                        ========> requestId : {}
                        ========> sessionId : {}
                        ========> remoteUser : {}
                        ========> remoteAddr : {}
                        ========> remoteHost : {}
                        ========> remotePort : {}
                        ========> http method : {}
                        ========> request url : {}
                        ========> character encoding : {}
                        ========> local : {}
                        ========> headers : {}
                        """,
                System.lineSeparator(),
                e,
                request.getRequestId(),
                request.getRequestedSessionId(),
                request.getRemoteUser(),
                request.getRemoteAddr(),
                request.getRemoteHost(),
                request.getRemotePort(),
                request.getMethod(),
                request.getRequestURL(),
                request.getCharacterEncoding(),
                request.getLocale(),
                headers
        );
    }

    private static StringBuilder convertHeaders(final HttpServletRequest request) {
        final StringBuilder headers = new StringBuilder();
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String headerName = headerNames.nextElement();
            final String headerValue = request.getHeader(headerName);
            headers.append("    ")
                    .append(headerName)
                    .append(":")
                    .append(headerValue)
                    .append(System.lineSeparator());
        }

        return headers;
    }

    private static String block(final StringBuilder sb) {
        if (sb.isEmpty()) {
            return "EMPTY";
        }
        sb.insert(0, "{")
                .insert(1, System.lineSeparator())
                .insert(sb.length(), "}");
        return sb.toString();
    }
}
