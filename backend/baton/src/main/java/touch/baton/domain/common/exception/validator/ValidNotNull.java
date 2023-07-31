package touch.baton.domain.common.exception.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import touch.baton.domain.common.exception.ClientErrorCode;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Repeatable(ValidNotNull.List.class)
@Documented
@Constraint(validatedBy = NotNullValidator.class)
public @interface ValidNotNull {

    String message() default "null 값이 존재합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    ClientErrorCode clientErrorCode();

    @Target({ FIELD, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        ValidNotNull[] value();
    }
}
