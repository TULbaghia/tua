package pl.lodz.p.it.ssbd2021.ssbd06.mappers.exceptions;

import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Mapuje wyjątek EJBException na odpowiedź HTTP
 */
@Provider
public class AppExceptionMapper implements ExceptionMapper<EJBException> {

    private final static String RESOURCE_FORBIDDEN = "error.rest.resource_forbidden";
    private final static String PROCESSING_ERROR = "error.rest.processing_error";

    /**
     * Odwzorowuje wyjątek EJBException i potomne na odpowiedź HTTP z statusem błędu:
     * <ul>
     *     <li>403 i odpowiednim komunikatem, gdy EJBAccessException</li>
     *     <li>500 i odpowiednim komunikatem w przeciwnym wypadku</li>
     * </ul>
     *
     * @param exception obiekt wyjątku biznesowego
     * @return odpowiedź HTTP
     */
    @Override
    public Response toResponse(EJBException exception) {
        if (exception instanceof EJBAccessException) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ExceptionResponse(RESOURCE_FORBIDDEN))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ExceptionResponse(PROCESSING_ERROR))
                .build();
    }
}
