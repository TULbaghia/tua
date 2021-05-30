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
 * Adnotacja służąca do weryfikacja długości hasła encji
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Size(min=8, max=64, message = "validation.password.size")
@Pattern(regexp = RegularExpression.PASSWORD_HEX, message = "validation.password.hex_pattern")
public @interface EntityPassword {
    String message() default "validation.password.size";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
