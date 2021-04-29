package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import pl.lodz.p.it.ssbd2021.ssbd06.auth.endpoints.AuthEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthController {

    @Inject
    private AuthEndpoint authEndpoint;

    /**
     * Przeprowadza proces uwierzytelnienia użytkownika
     *
     * @param login login użytkownika
     * @param password hasło użytkownika
     * @return token jwt
     */
    @POST
    @Path("auth")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public Response login(@NotNull @QueryParam("login") String login, @NotNull @QueryParam("password") String password) {
        try {
            String token = authEndpoint.login(login, password);
            return Response.accepted()
                    .type("application/json")
                    .entity(token)
                    .build();
        } catch (AppBaseException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
