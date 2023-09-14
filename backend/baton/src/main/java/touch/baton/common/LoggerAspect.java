package touch.baton.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Profile("!test")
@Aspect
@Component
public class LoggerAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void logInfo() {
    }

    @Before("logInfo()")
    public void requestLog(final JoinPoint joinPoint) {
        final HttpServletRequest request = getRequest();
        final String signatureName = getSignatureName(joinPoint);
        log.info(">>>>> API start [{}() from {}] by {} {}",
                signatureName, request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
    }

    @AfterReturning(value = "logInfo()", returning = "returnObj")
    public void after(final JoinPoint joinPoint, final Object returnObj) {
        final HttpServletRequest request = getRequest();
        final String signatureName = getSignatureName(joinPoint);
        log.info("\n>>>>> API finish [{}() from {}] by {} {} \n" +
                        ">>>>> API return value = {}",
                signatureName, request, request.getMethod(), request.getRequestURI(),
                returnObj);
    }

    @AfterThrowing(value = "logInfo()", throwing = "exception")
    public void afterThrowing(final JoinPoint joinPoint, final Exception exception) {
        final HttpServletRequest request = getRequest();
        final String signatureName = getSignatureName(joinPoint);
        log.warn("""
                \n
                >>>>> API ERROR [{}() from {}] by {} {}
                >>>>> ERROR MESSAGE = {}
                >>>>> STACK TRACE = {}
                """,
                signatureName, request.getRemoteAddr(), request.getMethod(), request.getRequestURI(),
                exception.getMessage(),
                convertPrettyStackTrace(exception.getStackTrace()));
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    private String getSignatureName(final JoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + joinPoint.getSignature().getName();
    }

    private String convertPrettyStackTrace(final StackTraceElement[] stackTraceElements) {
        return Arrays.stream(stackTraceElements)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }
}
