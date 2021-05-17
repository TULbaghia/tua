package pl.lodz.p.it.ssbd2021.ssbd06.utils.email;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.EmailException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.Config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Klasa oferująca funkcjonalność wysyłania wiadomości email, w reakcji na zdarzenia zachodzące w aplikacji.
 */
@ApplicationScoped
public class EmailSender {

    @Inject
    private Config emailConfig;

    /**
     * Wysyła link aktywacyjny po rejestracji konta użytkownika na podany email.
     *
     * @param account odbiorca wiadomości.
     * @param activationLink link do aktywacji konta użytkownika.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendActivationEmail(Account account, String activationLink) throws AppBaseException {
        String lang = account.getLanguage();
        String activationContent = emailConfig.getContentForType(lang, Config.MailType.ACTIVATE_ACCOUNT, account.getLogin(), activationLink);
        String activationSubject = emailConfig.getSubjectForType(lang, Config.MailType.ACTIVATE_ACCOUNT);
        sendEmail(account.getEmail(), activationSubject, activationContent);
    }

    /**
     * Wysyła wiadomość email z informacją o blokadzie konta użytkownika.
     *
     * @param account odbiorca wiadomości.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendLockAccountEmail(Account account) throws AppBaseException {
        String lang = account.getLanguage();
        String lockContent = emailConfig.getContentForType(lang, Config.MailType.LOCK_ACCOUNT, account.getLogin());
        String lockSubject = emailConfig.getSubjectForType(lang, Config.MailType.LOCK_ACCOUNT);
        sendEmail(account.getEmail(), lockSubject, lockContent);
    }

    /**
     * Wysyła wiadomość email z informacją o zdjęciu blokady konta użytkownika.
     *
     * @param account odbiorca wiadomości.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendUnlockAccountEmail(Account account) throws AppBaseException {
        String lang = account.getLanguage();
        String lockContent = emailConfig.getContentForType(lang, Config.MailType.UNLOCK_ACCOUNT, account.getLogin());
        String lockSubject = emailConfig.getSubjectForType(lang, Config.MailType.UNLOCK_ACCOUNT);
        sendEmail(account.getEmail(), lockSubject, lockContent);
    }

    /**
     * Wysyła wiadomość email z informacją o przydzieleniu poziomu dostępu do konta użytkownika.
     *
     * @param account odbiorca wiadomości.
     * @param accessLevel nazwa poziomu dostępu przydzielonego do konta użytkownika.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendGrantAccessLevelEmail(Account account, String accessLevel) throws AppBaseException {
        String lang = account.getLanguage();
        String grantAccessContent = emailConfig.getContentForType(lang, Config.MailType.GRANT_ACCESS, account.getLogin(), accessLevel);
        String grantAccessSubject = emailConfig.getSubjectForType(lang, Config.MailType.GRANT_ACCESS);
        sendEmail(account.getEmail(), grantAccessSubject, grantAccessContent);
    }

    /**
     * Wysyła wiadomość email z informacją o odebraniu poziomu dostępu z konta użytkownika.
     *
     * @param account odbiorca wiadomości.
     * @param accessLevel nazwa poziomu dostępu odbieranego od konta użytkownika.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendDenyAccessLevelEmail(Account account, String accessLevel) throws AppBaseException {
        String lang = account.getLanguage();
        String denyAccessContent = emailConfig.getContentForType(lang, Config.MailType.DENY_ACCESS, account.getLogin(), accessLevel);
        String denyAccessSubject = emailConfig.getSubjectForType(lang, Config.MailType.DENY_ACCESS);
        sendEmail(account.getEmail(), denyAccessSubject, denyAccessContent);
    }

    /**
     * Wysyła wiadomość email z linkiem rozpoczynającym procedurę resetowania hasła do konta użytkownika.
     *
     * @param account odbiorca wiadomości.
     * @param resetPasswordLink link do rozpoczęcia procedury resetowania hasła do konta użytkownika.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendResetPasswordEmail(Account account, String resetPasswordLink) throws AppBaseException {
        String lang = account.getLanguage();
        String resetContent = emailConfig.getContentForType(lang, Config.MailType.RESET_PASSWORD, account.getLogin(), resetPasswordLink);
        String resetSubject = emailConfig.getSubjectForType(lang, Config.MailType.RESET_PASSWORD);
        sendEmail(account.getEmail(), resetSubject, resetContent);
    }

    /**
     * Wysyła wiadomość email z informacją o usunięciu nieaktywowanego konta użytkownika.
     *
     * @param account odbiorca wiadomości.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendDeleteUnconfirmedAccountEmail(Account account) throws AppBaseException {
        String lang = account.getLanguage();
        String deleteUnconfirmedContent = emailConfig.getContentForType(lang, Config.MailType.DELETE_UNCONFIRMED, account.getLogin());
        String deleteUnconfirmedSubject = emailConfig.getSubjectForType(lang, Config.MailType.DELETE_UNCONFIRMED);
        sendEmail(account.getEmail(), deleteUnconfirmedSubject, deleteUnconfirmedContent);
    }

    /**
     * Przygotowuje elementy wysyłanej wiadomości email oraz wysyła wiadomość.
     * Tworzenie obiektu wiadomości: ustalenie nadawcy, odbiorcy oraz tematu i zawartości tekstowej wiadomości.
     *
     * @param userEmail adres email odbiorcy wiadomości.
     * @param subject temat wiadomości email.
     * @param content zawartość tekstowa wiadomości email.
     * @throws EmailException wysyłanie wiadomości email nie powiodło się.
     */
    private void sendEmail(String userEmail, String subject, String content) throws EmailException {
        try {
            MimeMessage message = new MimeMessage(prepareSession());
            message.setFrom(new InternetAddress(emailConfig.getMailHost()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject(subject);
            message.setText(content, "UTF-8", "html");
            Transport.send(message);
        } catch (MessagingException e) {
            throw EmailException.emailNotSent(e);
        }
    }

    /**
     * Przygotowuje instację sesji (javax.mail.Session).
     * Proces przygotowania sesji: tworzenie obiektu Properties i wczytanie wartości z deskryptora aplikacji.
     */
    private Session prepareSession() {
        Properties sessionProperties = new Properties();
        sessionProperties.put("mail.smtp.host", emailConfig.getMailHost());
        sessionProperties.put("mail.smtp.port", emailConfig.getMailPort());
        sessionProperties.put("mail.smtp.auth", "true");
        sessionProperties.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(sessionProperties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailConfig.getMailSender(),
                                emailConfig.getMailPassword());
                    }
                });
    }
}
