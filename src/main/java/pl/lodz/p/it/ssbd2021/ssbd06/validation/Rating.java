package pl.lodz.p.it.ssbd2021.ssbd06.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adnotacja służąca do weryfikacji oceny.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Min(value = 1, message = "validation.rating.bounds")
@Max(value = 5, message = "validation.rating.bounds")
public @interface Rating {
    String message() default "validation.rating.bounds";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
