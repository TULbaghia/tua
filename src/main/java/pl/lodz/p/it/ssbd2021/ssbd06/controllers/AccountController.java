package pl.lodz.p.it.ssbd2021.ssbd06.controllers;


import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RegisterAccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.AccountEndpointLocal;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
     * Zwraca listę wszystkich użytkowników systemu reprezentowanych jako DTO.
     *
     * @return lista użytkowników w formie DTO
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountDto> getAllAccountsList() throws AppBaseException {
        return accountEndpoint.getAllAccounts();
    }
}
