package touch.baton.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Slf4j
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

    @Around(value = "logInfo()")
    public Object printLog(final ProceedingJoinPoint joinPoint) {
        final HttpServletRequest request = getRequest();
        final String signatureName = getSignatureName(joinPoint);
        log.info(">>>>> API start [" + signatureName + "() from "
                + request.getRemoteAddr() + "] by "
                + request.getMethod() + " "
                + request.getRequestURI());

        final long startTime = System.currentTimeMillis();
        Object proceed = process(joinPoint, request, signatureName);
        final long timeDiff = System.currentTimeMillis() - startTime;
        log.info("시간차이(m) : {}", timeDiff);
        return proceed;
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    private String getSignatureName(final ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + joinPoint.getSignature().getName();
    }

    private Object process(final ProceedingJoinPoint joinPoint, final HttpServletRequest request, final String signatureName) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            log.error(">>>>> controller start [" + signatureName + "() from " + request.getRemoteAddr() + "] with Error[" + e.getMessage() + "]");
            throw new RuntimeException("에러 나요.");
        }
    }
}
