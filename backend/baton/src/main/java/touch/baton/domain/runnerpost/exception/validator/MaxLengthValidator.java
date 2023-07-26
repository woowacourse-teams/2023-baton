package touch.baton.domain.runnerpost.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

public class MaxLengthValidator implements ConstraintValidator<ValidMaxLength, String> {

    private ClientErrorCode errorCode;
    private int max;

    @Override
    public void initialize(final ValidMaxLength constraintAnnotation) {
        errorCode = constraintAnnotation.clientErrorCode();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value.length() > max) {
            throw new ClientRequestException(errorCode);
        }
        return true;
    }
}
