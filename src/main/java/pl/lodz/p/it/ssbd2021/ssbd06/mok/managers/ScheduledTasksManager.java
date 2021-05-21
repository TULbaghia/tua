package pl.lodz.p.it.ssbd2021.ssbd06.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.CodeType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.PendingCodeFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Startup
@Singleton
@Interceptors({LoggingInterceptor.class})
public class ScheduledTasksManager {

    @Resource
    TimerService timerService;
    @Inject
    private AccountFacade accountFacade;

    @Inject
    private PendingCodeFacade pendingCodeFacade;

    @Inject
    private EmailSender emailSender;

    /**
     * Usuwa konta użytkowników nie potwierdzonych
     *
     * @param time
     * @throws AppBaseException
     */
    @Schedule(hour = "*", minute = "0", second = "0", info = "Wykonuje metodę co godzinę począwszy od pełnej godziny")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void deleteUnverifiedAccounts(Timer time) throws AppBaseException {

        // usuwanie wszystkich niepotwierdzonych kont, które wygasły
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.HOUR, -24);
        Date expirationDateToRepeatEmail = calendar1.getTime();
        List<Account> unverifiedAccountsToRemove = accountFacade.findUnverifiedBefore(expirationDateToRepeatEmail);
        for (Account account : unverifiedAccountsToRemove) {
            accountFacade.remove(account);
        }

        // oznaczenie wygasłych kodów aktywacyjnych
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.HOUR, -12);
        Date codeExpirationDate = calendar2.getTime();
        List<PendingCode> unusedCodes = pendingCodeFacade.findAllUnusedByCodeTypeAndBefore(CodeType.ACCOUNT_ACTIVATION, codeExpirationDate);
        for (PendingCode code : unusedCodes) {
            pendingCodeFacade.remove(code);
        }

        // ponowne wysłanie maili na konta niepotwierdzonych użytkowników
        List<Account> accountList = accountFacade.findUnverifiedBetweenDate(expirationDateToRepeatEmail, codeExpirationDate);
        for (Account account : accountList) {
            account.getPendingCodeList().add(new PendingCode(UUID.randomUUID().toString(), false, CodeType.ACCOUNT_ACTIVATION, account, account));
//            sendRepeatedEmailNotification(account);
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
     *  W przypadku, gdy żeton zmiany email nie został użyty przez ponad godzinę od momentu utworzenia, jednak czas ten
     *  nie przekroczył 2 godzin od momentu utworzenia, następuje ponowne wysłanie emaila z informacją o rozpoczęciu
     *  procesu zmiany adresu email dla konta użytkownka.
     *
     *  W przypadku, gdy żeton zmiany email nie został użyty przez ponad 2 godziny od momentu utworzenia,
     *  następuje usunięcie żetonu.
     *
     * @param time
     * @throws AppBaseException
     */
    @Schedule(hour = "*", minute = "0", second = "0", info = "Wykonuje metodę co godzinę począwszy od pełnej godziny")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void sendRepeatedEmailChange(Timer time) throws AppBaseException {
        Calendar calendarRemoveCode = Calendar.getInstance();
        calendarRemoveCode.add(Calendar.HOUR, -2);
        List<Account> accountsUnusedCodes = pendingCodeFacade.findAllAccountsWithUnusedCodes(CodeType.EMAIL_CHANGE, calendarRemoveCode.getTime());
        for (Account account : accountsUnusedCodes) {
            PendingCode pc = pendingCodeFacade.findUnusedCodeByAccount(account, CodeType.EMAIL_CHANGE);
            pendingCodeFacade.remove(pc);
        }

        Calendar calendarRepeat = Calendar.getInstance();
        calendarRepeat.add(Calendar.HOUR, -1);
        accountsUnusedCodes = pendingCodeFacade.findAllAccountsWithUnusedCodes(CodeType.EMAIL_CHANGE, calendarRepeat.getTime());
        for (Account account : accountsUnusedCodes) {
            PendingCode pc = pendingCodeFacade.findUnusedCodeByAccount(account, CodeType.EMAIL_CHANGE);
            emailSender.sendEmailChange(account, pc.getCode());
        }
    }
}
