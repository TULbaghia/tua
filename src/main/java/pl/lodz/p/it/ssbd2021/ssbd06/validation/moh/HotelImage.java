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
 * Adnotacja służąca do weryfikacji obrazu hotelu.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 2, max = 127, message = "validation.hotel.image.size")
@Pattern(regexp = RegularExpression.HOTEL_IMAGE, message = "validation.hotel.image.pattern")
public @interface HotelImage {
    String message() default "validation.hotel.image.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
