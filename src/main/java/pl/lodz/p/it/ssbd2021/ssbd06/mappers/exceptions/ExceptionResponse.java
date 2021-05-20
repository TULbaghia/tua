package pl.lodz.p.it.ssbd2021.ssbd06.mappers.exceptions;

import java.util.Map;
import java.util.Set;

/**
 * Opakowuje komunikat błędu wysyłany do użytkownika w odpowiedzi HTTP
 */
public class ExceptionResponse {

    private final String message;
    private Map<String, Set<String>> constraints;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public ExceptionResponse(String message, Map<String, Set<String>> constraints) {
        this(message);
        this.constraints = constraints;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Set<String>> getConstraints() {
        return constraints;
    }
}
