package touch.baton.domain.runnerpost.exception.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import touch.baton.domain.common.exception.ClientErrorCode;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = FutureValidator.class)
public @interface ValidFuture {

    String message() default "마감일은 오늘보다 과거일 수 없습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    ClientErrorCode clientErrorCode();
}
