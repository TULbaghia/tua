package pl.lodz.p.it.ssbd2021.ssbd06.mok.services;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountAlreadyActivatedException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.CodeExpiredException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.PendingCodeFacade;

import javax.inject.Inject;

public class AccountService {

    @Inject
    private PendingCodeFacade pendingCodeFacade;

    @Inject
    private AccountFacade accountFacade;


    /**
     * Potwierdza konto użytkownika odpowiadające podanemu kodowi aktywacyjnemu
     *
     * @param code kod aktywacyjny konta
     * @throws CodeExpiredException gdy podany kod został już zużyty lub wygasł
     * @throws AccountAlreadyActivatedException gdy konto zostało już aktywowane
     */
    public void confirm(String code) throws CodeExpiredException, AccountAlreadyActivatedException {
        var pendingCode = pendingCodeFacade.findByCode(code);
        if(pendingCode.isUsed()){
            throw new CodeExpiredException("Code has been used or expired");
        }
        var account = pendingCode.getAccount();
        if(account.isConfirmed()){
            throw new AccountAlreadyActivatedException("Account already activated");
        }
        account.setConfirmed(true);
        pendingCode.setUsed(true);
        pendingCodeFacade.edit(pendingCode);
        accountFacade.edit(account);
    }
}
