package pl.lodz.p.it.ssbd2021.ssbd06.controllers;


import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.AccountEndpointLocal;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
public class AccountController extends AbstractController {

    @Inject
    private AccountEndpointLocal accountEndpoint;

    /**
     * Blokuje konto użytkownika o podanym loginie.
     *
     * @param login login konta, które ma zostać zablokowane.
     * @throws AppBaseException gdy nie udało się zablokowanie konta.
     */
    @PUT
    @Path("/{login}/block")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeAccountActiveStatus(@NotNull @PathParam("login") @Valid String login) throws AppBaseException {
//        metoda repeat() z abstractController;
        accountEndpoint.blockAccount(login);
    }

    @POST
    @Path("/confirm/{code}")
    public void confirm(@PathParam("code") String code) throws AppBaseException {
        // todo metoda repeat
        accountEndpoint.confirmAccount(code);
    }
}
