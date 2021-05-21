package pl.lodz.p.it.ssbd2021.ssbd06.mappers.exceptions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.*;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;


/**
 * Mapuje wyjątek ConstraintViolationException na odpowiedź HTTP
 */
@Provider
public class ConstraintValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private final static String REST_VALIDATION_ERROR = "error.rest.validation";

    /**
     * Odwzorowuje błędy walidacji pól na odpowiedź HTTP
     *
     * @param exception wyjątek BeanValidation
     * @return odpowiedź HTTP
     */
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, Set<String>> constraints = new HashMap<>();
        for (ConstraintViolation violation : exception.getConstraintViolations()) {
            String propertyName = getPropertyName(violation.getPropertyPath());
            Set<String> currentViolation = constraints.getOrDefault(propertyName, new HashSet<>());
            currentViolation.add(violation.getMessage());
            constraints.put(propertyName, currentViolation);
        }

        return Response.status(BAD_REQUEST)
                .entity(new ExceptionResponse(REST_VALIDATION_ERROR, constraints)).build();
    }

    private String getPropertyName(Path path) {
        String name = null;
        for (Path.Node node : path) {
            name = node.getName();
        }
        return name;
    }
}


