package pl.lodz.p.it.ssbd2021.ssbd06.security;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Udostępnia adnotację wiążącą metody z filtrem sprawdzającym poprawność nagłówka If-Match
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface EtagValidatorFilterBinding {
}
