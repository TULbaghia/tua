package pl.lodz.p.it.ssbd2021.ssbd06.mappers.exceptions;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Mapuje wyjątek AppBaseException na odpowiedź HTTP
 */
@Provider
public class AppBaseExceptionMapper implements ExceptionMapper<AppBaseException> {

    /**
     * Odwzorowuje wyjątek AppBaseException i potomne na odpowiedź HTTP z statusem błędu 400
     * i wiadomością z rzucanego wyjątku
     *
     * @param exception obiekt wyjątku biznesowego
     * @return odpowiedź HTTP
     */
    @Override
    public Response toResponse(AppBaseException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionResponse(exception.getMessage())).build();
    }
}
