package touch.baton.tobe.domain.runnerpost.command.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

import java.time.LocalDateTime;
import java.util.Objects;

public class FutureValidator implements ConstraintValidator<ValidFuture, LocalDateTime> {

    private ClientErrorCode errorCode;

    @Override
    public void initialize(final ValidFuture constraintAnnotation) {
        errorCode = constraintAnnotation.clientErrorCode();
    }

    @Override
    public boolean isValid(final LocalDateTime value, final ConstraintValidatorContext context) {
        if (Objects.nonNull(value) && value.isBefore(LocalDateTime.now())) {
            throw new ClientRequestException(errorCode);
        }
        return true;
    }
}
