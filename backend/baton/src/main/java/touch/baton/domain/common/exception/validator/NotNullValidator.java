package touch.baton.domain.common.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

public class NotNullValidator implements ConstraintValidator<ValidNotNull, Object> {

    private ClientErrorCode errorCode;

    @Override
    public void initialize(final ValidNotNull constraintAnnotation) {
        errorCode = constraintAnnotation.clientErrorCode();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        if (value == null) {
            throw new ClientRequestException(errorCode);
        }
        return true;
    }
}
