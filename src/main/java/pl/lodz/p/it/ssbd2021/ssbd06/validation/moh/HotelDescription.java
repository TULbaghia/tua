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
 * Adnotacja służąca do weryfikacji opisu hotelu.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 8, max = 511, message = "validation.hotel.description.size")
@Pattern(regexp = RegularExpression.HOTEL_DESCRIPTION, message = "validation.hotel.description.pattern")
public @interface HotelDescription {
    String message() default "validation.hotel.description.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
