package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import pl.lodz.p.it.ssbd2021.ssbd06.auth.dto.LoginDataDto;
import pl.lodz.p.it.ssbd2021.ssbd06.auth.endpoints.AuthEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AuthValidationException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.AccountEndpointLocal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.Date;

@Path("/auth")
public class AuthController extends AbstractController {

    @Inject
    private AuthEndpointLocal authEndpoint;

    @Inject
    private AccountEndpointLocal accountEndpoint;

    @Context
    private HttpServletRequest httpServletRequest;

    /**
     * Przeprowadza proces uwierzytelnienia użytkownika.
     *
     * @param loginDataDto objekt zawierający login i hasło użytkownika
     * @return w przypadku powodzenia operacji uwierzytelnienia token jwt
     * @throws AppBaseException gdy aktualizacja danych dotyczących uwierzytelnienia się nie powiedzie
     */
    @POST
    @Path("auth")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public Response login(@NotNull @Valid LoginDataDto loginDataDto) throws AppBaseException {
        try {
            String token = repeat(() -> authEndpoint.login(loginDataDto.getLogin(), loginDataDto.getPassword()), authEndpoint);
            repeat(() -> accountEndpoint.updateValidAuth(loginDataDto.getLogin(), httpServletRequest.getRemoteAddr(), Date.from(Instant.now())), accountEndpoint);
            return Response.accepted()
                    .type("application/json")
                    .entity(token)
                    .build();
        } catch (AuthValidationException e) {
            repeat(() -> accountEndpoint.updateInvalidAuth(loginDataDto.getLogin(), httpServletRequest.getRemoteAddr(), Date.from(Instant.now())), accountEndpoint);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * Wywoływany w celu zapisania w dzienniku zdarzeń faktu wylogowania użytkownika
     *
     * @return kod odpowiedzi HTTP 200
     */
    @GET
    @Path("logout")
    public Response logout() {
        authEndpoint.logout();
        return Response.ok().build();
    }
}
