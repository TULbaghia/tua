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

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.UUID;

/**
 * Odpowiada za przetwarzanie logiki biznesowej związanej z obsługą kodów aktywacyjnych
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PendingCodeManager {
    @Inject
    private EmailSender emailSender;

    @Inject
    private PendingCodeFacade pendingCodeFacade;

    @Inject
    private AccountFacade accountFacade;

    /**
     * Tworzy nowy kod resetu hasła i wysyła wiadomość na e-mail odpowiadający kontu.
     *
     * @param email email konta, którego resetowanie hasła dotyczy
     * @throws AppBaseException - jeżeli nie uda się wysłać maila
     */
    @PermitAll
    public void sendResetPassword(String email) throws AppBaseException {
        Account account = accountFacade.findByEmail(email);
        if (!account.isEnabled()) {
            throw AccountException.notEnabled();
        }
        if (!account.isConfirmed()) {
            throw AccountException.notConfirmed();
        }
        List<PendingCode> previousResetCodes = pendingCodeFacade.findResetCodesByAccount(account);
        if (previousResetCodes.size() > 0) {
            for (int i = 0; i <= previousResetCodes.size() - 1; i++) {
                PendingCode previousResetCode = previousResetCodes.get(i);
                previousResetCode.setUsed(true);
                pendingCodeFacade.edit(previousResetCode);
            }
        }

        PendingCode pendingCode = preparePendingCode(account);
        emailSender.sendResetPasswordEmail(account, pendingCode.getCode());
    }

    /**
     * Ponownie wysyła wiadomość dotyczącą resetowania hasła na e-mail odpowiadający kontu.
     *
     * @param email email konta, którego resetowanie hasła dotyczy
     * @throws AppBaseException - jeżeli nie uda się wysłać maila
     */
    @PermitAll
    public void sendResetPasswordAgain(String email) throws AppBaseException {
        Account account = accountFacade.findByEmail(email);
        if (!account.isEnabled()) throw AccountException.notEnabled();
        if (!account.isConfirmed()) throw AccountException.notConfirmed();

        PendingCode previousResetCode = pendingCodeFacade.findResetCodesByAccount(account).get(0);
        previousResetCode.setUsed(true);
        pendingCodeFacade.edit(previousResetCode);

        PendingCode pendingCode = preparePendingCode(account);
        emailSender.sendResetPasswordEmail(account, pendingCode.getCode());
    }

    /**
     * Przygotowuje kod resetujący hasło dla podanego konta
     *
     * @param account konto użytkownika, którego dotyczy resetowanie hasła
     * @return kod resetujący hasło
     * @throws AppBaseException w sytuacji niepowodzenia utworzenia kodu resetującego hasło
     */
    private PendingCode preparePendingCode(Account account) throws AppBaseException {
        PendingCode pendingCode = new PendingCode();
        pendingCode.setCode(UUID.randomUUID().toString());
        pendingCode.setUsed(false);
        pendingCode.setAccount(account);
        pendingCode.setCodeType(CodeType.PASSWORD_RESET);
        pendingCode.setCreatedBy(account);

        account.getPendingCodeList().add(pendingCode);

        pendingCodeFacade.create(pendingCode);

        return pendingCode;
    }
}
