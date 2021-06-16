package pl.lodz.p.it.ssbd2021.ssbd06.validation.moh;

import pl.lodz.p.it.ssbd2021.ssbd06.validation.RegularExpression;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja służąca do weryfikacji opisu boxu.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 1, max = 31, message = "validation.box.description.size")
@Pattern(regexp = RegularExpression.BOX_DESCRIPTION, message = "validation.box.description.pattern")
public @interface BoxDescription {
    String message() default "validation.box.description.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
