package pl.lodz.p.it.ssbd2021.ssbd06.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja służąca do weryfikacji kodu językowego.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 2, max = 2, message = "{validation.language.size}")
@Pattern(regexp = RegularExpression.LANGUAGE_CODE, message = "{validation.language.pattern}")
public @interface Language {
    String message() default "{validation.language.pattern}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
