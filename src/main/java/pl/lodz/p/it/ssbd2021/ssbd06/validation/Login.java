package pl.lodz.p.it.ssbd2021.ssbd06.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 6, max = 127, message = "{validation.login.size}")
@Email(message = "{validation.login.pattern}")
public @interface Login {
    String message() default "{validation.login.pattern}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
