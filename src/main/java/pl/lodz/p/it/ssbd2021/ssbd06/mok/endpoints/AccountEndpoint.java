package pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.endpoints.interfaces.AccountEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.services.AccountService;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountEndpoint implements AccountEndpointLocal {

    @Inject
    private AccountService accountService;

    @Override
    public void confirmAccount(String code) throws AppBaseException {
        accountService.confirm(code);
    }
}
