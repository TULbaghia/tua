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
 * Adnotacja służąca do weryfikacji nr telefonu.
 */

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 9, max = 15, message = "{validation.contactnumber.size}")
@Pattern(regexp = RegularExpression.CONTACT_NUMBER, message = "{validation.contactnumber.pattern}")
public @interface ContactNumber {
    String message() default "{validation.contactnumber.pattern}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
