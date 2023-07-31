package touch.baton.domain.runnerpost.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

import java.time.LocalDateTime;

public class FutureValidator implements ConstraintValidator<ValidFuture, LocalDateTime> {

    private ClientErrorCode errorCode;

    @Override
    public void initialize(final ValidFuture constraintAnnotation) {
        errorCode = constraintAnnotation.clientErrorCode();
    }

    @Override
    public boolean isValid(final LocalDateTime value, final ConstraintValidatorContext context) {
        if (value.isBefore(LocalDateTime.now())) {
            throw new ClientRequestException(errorCode);
        }
        return true;
    }
}
