package pl.lodz.p.it.ssbd2021.ssbd06.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.CodeType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.PendingCodeFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;

@Startup
@Singleton
public class ScheduledTasksManager {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private PendingCodeFacade pendingCodeFacade;

    @Inject
    private EmailSender emailSender;

    @Resource
    TimerService timerService;

    /**
     *  Usuwa konta użytkowników nie potwierdzonych
     *
     * @param time
     * @throws AppBaseException
     */
    @Schedule(hour = "*", minute = "0", second = "0", info = "Wykonuje metodę co godzinę począwszy od pełnej godziny")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void deleteUnverifiedAccounts(Timer time) throws AppBaseException {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.HOUR, -24);
        long expirationDate = calendar1.getTimeInMillis();
        List<Account> unverifiedAccountListAfter24H = accountFacade.findUnverifiedBefore(expirationDate);
        for(Account account: unverifiedAccountListAfter24H){
            accountFacade.remove(account);
        }
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.HOUR, -12);
        long expirationDateToRepeatEmail = calendar2.getTimeInMillis();
        List<Account> unverifiedAccountListAfter12H = accountFacade.findUnverifiedBefore(expirationDateToRepeatEmail);
        for(Account account: unverifiedAccountListAfter12H){
            sendRepeatedEmailNotification(account);
        }

    }

    /**
     * Wysyła powtórnie mail aktywacyjny do użytkownika
     *
     * @throws AppBaseException gdy dane konto nie istnieje lub jest zweryfikowane
     */
    public void sendRepeatedEmailNotification(Account account) throws AppBaseException {
        var activationCode = pendingCodeFacade.findNotUsedByAccount(account);
        emailSender.sendActivationEmail(account, activationCode.getCode());
    }

    /**
     *  Ponownie wysyła email z informacją o rozpoczęciu procesu zmiany adresu email dla konta użytkownka.
     *
     * @param time
     * @throws AppBaseException
     */
    @Schedule(hour = "*", minute = "0", second = "0", info = "Wykonuje metodę co godzinę począwszy od pełnej godziny")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void sendRepeatedEmailChange(Timer time) throws AppBaseException {
        Calendar calendarRemoveCode = Calendar.getInstance();
        calendarRemoveCode.add(Calendar.HOUR, -2);
        long expirationDate = calendarRemoveCode.getTimeInMillis();
        List<Account> accountsUnusedCodes = pendingCodeFacade.findAllAccountsWithUnusedCodes(CodeType.EMAIL_CHANGE, expirationDate);
        for (Account account : accountsUnusedCodes) {
            PendingCode pc = pendingCodeFacade.findUnusedCodeByAccount(account, CodeType.EMAIL_CHANGE);
            pendingCodeFacade.remove(pc);
        }

        Calendar calendarRepeat = Calendar.getInstance();
        calendarRepeat.add(Calendar.HOUR, -1);
        expirationDate = calendarRepeat.getTimeInMillis();
        accountsUnusedCodes = pendingCodeFacade.findAllAccountsWithUnusedCodes(CodeType.EMAIL_CHANGE, expirationDate);
        for (Account account : accountsUnusedCodes) {
            PendingCode pc = pendingCodeFacade.findUnusedCodeByAccount(account, CodeType.EMAIL_CHANGE);
            emailSender.sendEmailChange(account, pc.getCode());
        }
    }
}
