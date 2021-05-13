package pl.lodz.p.it.ssbd2021.ssbd06.utils.email;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.EmailException;

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
    private EmailConfig emailConfig;

    /**
     * Wysyła link aktywacyjny po rejestracji konta użytkownika na podany email.
     *
     * @param account odbiorca wiadomości.
     * @param activationLink link do aktywacji konta użytkownika.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendActivationEmail(Account account, String activationLink) throws AppBaseException {
        String lang = account.getLanguage();
        String activationContent = emailConfig.getContentForType(lang, EmailConfig.MailType.ACTIVATE_ACCOUNT, account.getFirstname(), activationLink);
        String activationSubject = emailConfig.getSubjectForType(lang, EmailConfig.MailType.ACTIVATE_ACCOUNT);
        sendEmail(account.getLogin(), activationSubject, activationContent);
    }

    /**
     * Wysyła wiadomość email z informacją o blokadzie konta użytkownika.
     *
     * @param account odbiorca wiadomości.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendLockAccountEmail(Account account) throws AppBaseException {
        String lang = account.getLanguage();
        String lockContent = emailConfig.getContentForType(lang, EmailConfig.MailType.LOCK_ACCOUNT, account.getFirstname());
        String lockSubject = emailConfig.getSubjectForType(lang, EmailConfig.MailType.LOCK_ACCOUNT);
        sendEmail(account.getLogin(), lockSubject, lockContent);
    }

    /**
     * Wysyła wiadomość email z informacją o zdjęciu blokady konta użytkownika.
     *
     * @param account odbiorca wiadomości.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendUnlockAccountEmail(Account account) throws AppBaseException {
        String lang = account.getLanguage();
        String lockContent = emailConfig.getContentForType(lang, EmailConfig.MailType.UNLOCK_ACCOUNT, account.getFirstname());
        String lockSubject = emailConfig.getSubjectForType(lang, EmailConfig.MailType.UNLOCK_ACCOUNT);
        sendEmail(account.getLogin(), lockSubject, lockContent);
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
        String grantAccessContent = emailConfig.getContentForType(lang, EmailConfig.MailType.GRANT_ACCESS, account.getFirstname(), accessLevel);
        String grantAccessSubject = emailConfig.getSubjectForType(lang, EmailConfig.MailType.GRANT_ACCESS);
        sendEmail(account.getLogin(), grantAccessSubject, grantAccessContent);
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
        String denyAccessContent = emailConfig.getContentForType(lang, EmailConfig.MailType.DENY_ACCESS, account.getFirstname(), accessLevel);
        String denyAccessSubject = emailConfig.getSubjectForType(lang, EmailConfig.MailType.DENY_ACCESS);
        sendEmail(account.getLogin(), denyAccessSubject, denyAccessContent);
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
        String resetContent = emailConfig.getContentForType(lang, EmailConfig.MailType.RESET_PASSWORD, account.getFirstname(), resetPasswordLink);
        String resetSubject = emailConfig.getSubjectForType(lang, EmailConfig.MailType.RESET_PASSWORD);
        sendEmail(account.getLogin(), resetSubject, resetContent);
    }

    /**
     * Wysyła wiadomość email z informacją o usunięciu nieaktywowanego konta użytkownika.
     *
     * @param account odbiorca wiadomości.
     * @throws AppBaseException wysyłanie wiadomości email nie powiodło się.
     */
    public void sendDeleteUnconfirmedAccountEmail(Account account) throws AppBaseException {
        String lang = account.getLanguage();
        String deleteUnconfirmedContent = emailConfig.getContentForType(lang, EmailConfig.MailType.DELETE_UNCONFIRMED, account.getFirstname());
        String deleteUnconfirmedSubject = emailConfig.getSubjectForType(lang, EmailConfig.MailType.DELETE_UNCONFIRMED);
        sendEmail(account.getLogin(), deleteUnconfirmedSubject, deleteUnconfirmedContent);
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
            message.setFrom(new InternetAddress(emailConfig.getConfigProperty("host")));
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
        sessionProperties.put("mail.smtp.host", emailConfig.getConfigProperty("host"));
        sessionProperties.put("mail.smtp.port", emailConfig.getConfigProperty("port"));
        sessionProperties.put("mail.smtp.auth", "true");
        sessionProperties.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(sessionProperties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailConfig.getConfigProperty("sender"),
                                emailConfig.getConfigProperty("password"));
                    }
                });
    }
}
