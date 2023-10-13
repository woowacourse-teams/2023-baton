package touch.baton.domain.runnerpost.command.exception.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import touch.baton.domain.common.exception.ClientErrorCode;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UrlValidator.class)
public @interface ValidNotUrl {

    String message() default "PR 주소가 URL이 아닙니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    ClientErrorCode clientErrorCode();
}
