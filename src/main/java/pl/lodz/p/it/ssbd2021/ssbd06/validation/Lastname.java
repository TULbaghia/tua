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
 * Adnotacja służąca do weryfikacji nazwiska.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 2, max = 31, message = "validation.lastname.size")
@Pattern(regexp = RegularExpression.LASTNAME, message = "validation.lastname.pattern")
public @interface Lastname {
    String message() default "validation.lastname.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
