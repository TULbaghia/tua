package pl.lodz.p.it.ssbd2021.ssbd06.mappers.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * Mapuje obiekty dziedziczące z Throwable na odpowiedź HTTP
 */
@Provider
public class UncaughtExceptionMapper implements ExceptionMapper<Throwable> {

    /**
     * Odwzorowuje obiekt Throwable i potomne na odpowiedź HTTP z statusem błędu 500
     *
     * @param throwable obiekt throwable
     * @return odpowiedź HTTP
     */
    @Override
    public Response toResponse(Throwable throwable) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ExceptionResponse("internal_server_error"))
                .build();
    }
}
