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
 * Adnotacja służąca do weryfikacji opisu miasta.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 8, max = 511, message = "validation.city.description.size")
@Pattern(regexp = RegularExpression.CITY_DESCRIPTION, message = "validation.city.description.pattern")
public @interface CityDescription {
    String message() default "validation.city.description.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
