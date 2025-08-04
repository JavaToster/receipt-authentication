package com.example.authentication.validator;


import com.example.authentication.DTO.auth.AuthenticationDataDTO;
import com.example.authentication.utilServices.ErrorMessageCreator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class AuthValidator {
    private final ErrorMessageCreator creator;
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX);
    public void validate(BindingResult errors){
        if (errors.hasErrors()){
            throw new ValidationException(creator.createErrorMessage(errors));
        }
    }

    public void validateAllFields(AuthenticationDataDTO authData) {
        boolean hasTelegramId = authData.getTelegramId() != 0;
        boolean hasPassword   = authData.getPassword() != null
                && !authData.getPassword().isBlank();
        boolean hasEmail      = authData.getRecoveryEmail() != null
                && !authData.getRecoveryEmail().isBlank()
                && isValidEmail(authData.getRecoveryEmail());

        if (!(hasTelegramId || hasPassword || hasEmail)) {
            throw new ValidationException(
                    "Not correct authentication data, please check or enter another data"
            );
        }
    }

    /**
     * Проверяет строку email на соответствие регулярному выражению.
     */
    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
}
