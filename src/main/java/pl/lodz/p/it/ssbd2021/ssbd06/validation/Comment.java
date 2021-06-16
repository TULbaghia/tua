package pl.lodz.p.it.ssbd2021.ssbd06.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja służąca do weryfikacji komentarza.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 4, max = 255, message = "validation.comment.bounds")
public @interface Comment {
    String message() default "validation.comment.bounds";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


