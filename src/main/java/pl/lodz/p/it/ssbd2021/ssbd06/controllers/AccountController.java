package pl.lodz.p.it.ssbd2021.ssbd06.controllers;


import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RolesDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.AccountEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RegisterAccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.RoleEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.security.MessageSigner;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Login;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
public class AccountController extends AbstractController {

    @Inject
    private AccountEndpointLocal accountEndpoint;

    @Inject
    private RoleEndpointLocal roleEndpoint;

    @Inject
    private MessageSigner messageSigner;

    /**
     * Blokuje konto użytkownika o podanym loginie.
     *
     * @param login login konta, które ma zostać zablokowane.
     * @throws AppBaseException gdy nie udało się zablokowanie konta.
     */
    @PUT
    @Path("/{login}/block")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeAccountActiveStatus(@NotNull @Login @PathParam("login") @Valid String login) throws AppBaseException {
        // todo metoda repeat() z abstractController;
        accountEndpoint.blockAccount(login);
    }

    /**
     * Potwierdza konto użytkownika odpowiadające podanemu kodowi aktywacyjnemu
     *
     * @param code kod aktywacyjny konta
     * @throws AppBaseException gdzy potwierdzenie konta się nie powiodło
     */
    @POST
    @Path("/confirm/{code}")
    public void confirm(@PathParam("code") String code) throws AppBaseException {
        // todo metoda repeat() z abstractController;
        accountEndpoint.confirmAccount(code);
    }

    /**
     * Rejestruje konto użytkownika w systemie.
     *
     * @param registerAccountDto dane użytkownika do rejestracji
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registerAccount(@NotNull @Valid RegisterAccountDto registerAccountDto) throws AppBaseException {
        accountEndpoint.registerAccount(registerAccountDto);
    }

    /**
     * Zwraca listę ról przypisanych do użytkownika.
     *
     * @param login login użytkownika
     * @return lista ról przypisana do użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @GET
    @Path("/{login}/role")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRole(@NotNull @Login @PathParam("login") String login) throws AppBaseException {
        RolesDto rolesDto = roleEndpoint.getUserRole(login);
        return Response.ok()
                .entity(rolesDto)
                .tag(messageSigner.sign(rolesDto))
                .build();
    }

    /**
     * Zwraca listę ról zalogowanego użytkownika.
     *
     * @return lista ról przypisana do użytkownika
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @GET
    @Path("/self/role")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRole() throws AppBaseException {
        RolesDto rolesDto = roleEndpoint.getUserRole();
        return Response.ok()
                .entity(rolesDto)
                .tag(messageSigner.sign(rolesDto))
                .build();
    }

    /**
     * Przyznaje uprawnienia użytkownikowi.
     *
     * @param login       identyfikator użytkownika
     * @param accessLevel rola, która zostanie przydzielona użytkownikowi
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PATCH
    @Path("/{login}/grant/{accessLevel}")
    public void grantAccessLevel(@NotNull @Login @PathParam("login") String login, @NotNull @PathParam("accessLevel") AccessLevel accessLevel) throws AppBaseException {
        roleEndpoint.grantAccessLevel(login, accessLevel);
    }
}
