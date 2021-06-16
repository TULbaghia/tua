package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import pl.lodz.p.it.ssbd2021.ssbd06.auth.dto.LoginDataDto;
import pl.lodz.p.it.ssbd2021.ssbd06.auth.endpoints.AuthEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AuthValidationException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.AccountEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.security.JWTGenerator;

import javax.annotation.security.RolesAllowed;
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

/**
 * Umożliwia proces uwierzytelnienia oraz wylogowania.
 */
@Path("/auth")
public class AuthController extends AbstractController {

    @Inject
    private JWTGenerator jwtGenerator;

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
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    @Operation(operationId = "login", summary = "login")
    public Response login(@NotNull @Valid LoginDataDto loginDataDto) throws AppBaseException {
        try {
            String token = authEndpoint.login(loginDataDto);
            repeat(() -> accountEndpoint.updateValidAuth(loginDataDto.getLogin(), httpServletRequest.getRemoteAddr(),
                    Date.from(Instant.now())), accountEndpoint);

            if(jwtGenerator.getCallerGroups(token).contains(AccessLevel.ADMIN.name())) {
                repeat(() -> accountEndpoint.sendAdminLoginInfo(loginDataDto.getLogin(), httpServletRequest.getRemoteAddr()),
                        accountEndpoint);
            }

            return Response.accepted()
                    .type("application/json")
                    .entity(token)
                    .build();
        } catch (AuthValidationException e) {
            try {
                repeat(() -> accountEndpoint.updateInvalidAuth(loginDataDto.getLogin(), httpServletRequest.getRemoteAddr(),
                        Date.from(Instant.now())), accountEndpoint);
            } catch (NotFoundException notFoundException) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * Wywoływany w celu zapisania w dzienniku zdarzeń faktu wylogowania użytkownika
     *
     * @return kod odpowiedzi HTTP 200
     */
    @GET
    @RolesAllowed("logoutUser")
    @Path("logout")
    @Operation(operationId = "logout", summary = "logout")
    public Response logout() {
        authEndpoint.logout();
        return Response.ok().build();
    }

    /**
     * Odświeża token JWT
     * @param token obecny token, wymagający odnowienia
     * @return nowy token JWT
     */
    @POST
    @Path("/refresh-token")
    @Operation(operationId = "refreshToken", summary = "refreshToken")
    public Response refreshToken(@NotNull String token){
        String refreshedToken = authEndpoint.refreshToken(token);
        return Response.ok().entity(refreshedToken).build();
    }
}
