package pl.lodz.p.it.ssbd2021.ssbd06.mok.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.CodeType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.facades.PendingCodeFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.email.EmailSender;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.ServletContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    @Inject
    private ServletContext context;


    /**
     * Usuwa konta użytkowników nie potwierdzonych
     *
     * @param time
     * @throws AppBaseException
     */
    @Schedule(hour = "*", minute = "0", second = "0", info = "Wykonuje metodę co godzinę począwszy od pełnej godziny")
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void deleteUnverifiedAccounts(Timer time) throws AppBaseException {
        int confirmationCodeExpirationTime = Integer
                .parseInt(context.getInitParameter("accountConfirmationCodeExpirationTime"));
        int confirmationCodeHalfOfExpirationTime = confirmationCodeExpirationTime / 2;

        Instant expirationInstant = Instant.now().minus(confirmationCodeExpirationTime, ChronoUnit.HOURS);
        Date expirationDate = Date.from(expirationInstant);

        List<Account> unverifiedAccountsToRemove = accountFacade.findUnverifiedBefore(expirationDate);
        for (Account account : unverifiedAccountsToRemove) {
            accountFacade.remove(account);
            emailSender.sendDeleteUnconfirmedAccountEmail(account);
        }

        // ponowne wysłanie kodów aktywacyjnych
        Instant halfExpirationInstant = Instant.now().minus(confirmationCodeHalfOfExpirationTime, ChronoUnit.HOURS);
        Date halfExpirationDate = Date.from(halfExpirationInstant);
        List<PendingCode> unusedCodes = pendingCodeFacade.findAllUnusedByCodeTypeAndBeforeAndAttemptCount(CodeType.ACCOUNT_ACTIVATION, halfExpirationDate, 0);
        for (PendingCode code : unusedCodes) {
            code.setSendAttempt(code.getSendAttempt() + 1);
            pendingCodeFacade.edit(code);
            emailSender.sendActivationEmail(code.getAccount(), code.getCode());
        }
    }

    /**
     * Ponownie wysyła email z informacją o rozpoczęciu procesu zmiany adresu email dla konta użytkownka.
     * W przypadku, gdy żeton zmiany email nie został użyty przez ponad godzinę od momentu utworzenia, jednak czas ten
     * nie przekroczył 2 godzin od momentu utworzenia, następuje ponowne wysłanie emaila z informacją o rozpoczęciu
     * procesu zmiany adresu email dla konta użytkownka.
     * <p>
     * W przypadku, gdy żeton zmiany email nie został użyty przez ponad 2 godziny od momentu utworzenia,
     * następuje usunięcie żetonu.
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
