package pl.lodz.p.it.ssbd2021.ssbd06.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.AccountFacade;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.List;

@Startup
@Singleton
public class ScheduledTasksManager {

    @Inject
    private AccountFacade accountFacade;


    /**
     *  Usuwa konta użytkowników nie potwierdzonych
     *
     * @param time
     * @throws AppBaseException
     */
    @Schedule(info = "Usuwa nieaktywne konta")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void deleteUnverifiedAccounts(Timer time) throws AppBaseException {
        List<Account> unverifiedAccountList = accountFacade.findUnverifiedBefore();
        for(Account account: unverifiedAccountList){
            deleteUnverifiedAccount(account);
        }
    }

    /**
     * Usuwa niezweryfikowane konto użytkownika
     *
     * @throws AppBaseException gdy dane konto nie istnieje lub jest zweryfikowane
     */
    public void deleteUnverifiedAccount(Account account) throws AppBaseException {
        if(!account.isConfirmed()){
            accountFacade.remove(account);
            // todo remove pending codes ?
        }else{
            throw AccountException.confirmed();
        }
    }
}
