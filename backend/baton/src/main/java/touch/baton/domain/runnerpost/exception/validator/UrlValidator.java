package touch.baton.domain.runnerpost.exception.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

import java.util.Objects;
import java.util.regex.Pattern;

public class UrlValidator implements ConstraintValidator<ValidNotUrl, String> {

    private static final Pattern urlPattern = Pattern.compile("(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");

    private ClientErrorCode errorCode;

    @Override
    public void initialize(final ValidNotUrl constraintAnnotation) {
        errorCode = constraintAnnotation.clientErrorCode();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (Objects.nonNull(value) && isNotUrl(value)) {
            throw new ClientRequestException(errorCode);
        }

        return true;
    }

    private boolean isNotUrl(final String value) {
        return !urlPattern.matcher(value).matches();
    }
}
