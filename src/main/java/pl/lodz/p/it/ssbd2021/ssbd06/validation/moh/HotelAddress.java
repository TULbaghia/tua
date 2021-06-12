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
 * Adnotacja służąca do weryfikacji adresu hotelu.
 */
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 2, max = 63, message = "validation.hotel.address.size")
@Pattern(regexp = RegularExpression.HOTEL_ADDRESS, message = "validation.hotel.address.pattern")
public @interface HotelAddress {
    String message() default "validation.hotel.address.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
