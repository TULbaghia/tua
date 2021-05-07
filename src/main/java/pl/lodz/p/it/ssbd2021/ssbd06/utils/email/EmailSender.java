package pl.lodz.p.it.ssbd2021.ssbd06.utils.email;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.EmailException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.EmailConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public void sendActivationEmail(String userName, String userEmail, String activationLink) throws AppBaseException {
        String activationContent = EmailConfig.getContentForType(EmailConfig.MailType.ACTIVATE_ACCOUNT, userName, activationLink);
        String activationSubject = EmailConfig.getSubjectForType(EmailConfig.MailType.ACTIVATE_ACCOUNT);
        sendEmail(userEmail, activationSubject, activationContent);
    }

    public void sendLockAccountEmail(String userName, String userEmail) throws AppBaseException {
        String lockContent = EmailConfig.getContentForType(EmailConfig.MailType.LOCK_ACCOUNT, userName);
        String lockSubject = EmailConfig.getSubjectForType(EmailConfig.MailType.LOCK_ACCOUNT);
        sendEmail(userEmail, lockSubject, lockContent);
    }

    public void sendUnlockAccountEmail(String userName, String userEmail) throws AppBaseException {
        String lockContent = EmailConfig.getContentForType(EmailConfig.MailType.UNLOCK_ACCOUNT, userName);
        String lockSubject = EmailConfig.getSubjectForType(EmailConfig.MailType.UNLOCK_ACCOUNT);
        sendEmail(userEmail, lockSubject, lockContent);
    }

    public void sendGrantAccessLevelEmail(String userName, String userEmail, String accessLevel) throws AppBaseException {
        String grantAccessContent = EmailConfig.getContentForType(EmailConfig.MailType.GRANT_ACCESS, userName, accessLevel);
        String grantAccessSubject = EmailConfig.getSubjectForType(EmailConfig.MailType.GRANT_ACCESS);
        sendEmail(userEmail, grantAccessSubject, grantAccessContent);
    }

    public void sendDenyAccessLevelEmail(String userName, String userEmail, String accessLevel) throws AppBaseException {
        String denyAccessContent = EmailConfig.getContentForType(EmailConfig.MailType.DENY_ACCESS, userName, accessLevel);
        String denyAccessSubject = EmailConfig.getSubjectForType(EmailConfig.MailType.DENY_ACCESS);
        sendEmail(userEmail, denyAccessSubject, denyAccessContent);
    }

    public void sendResetPasswordEmail(String userName, String userEmail, String resetPasswordLink) throws AppBaseException {
        String resetContent = EmailConfig.getContentForType(EmailConfig.MailType.RESET_PASSWORD, userName, resetPasswordLink);
        String resetSubject = EmailConfig.getSubjectForType(EmailConfig.MailType.RESET_PASSWORD);
        sendEmail(userEmail, resetSubject, resetContent);
    }

    public void sendDeleteUnconfirmedAccountEmail(String userName, String userEmail) throws AppBaseException {
        String deleteUnconfirmedContent = EmailConfig.getContentForType(EmailConfig.MailType.DELETE_UNCONFIRMED, userName);
        String deleteUnconfirmedSubject = EmailConfig.getSubjectForType(EmailConfig.MailType.DELETE_UNCONFIRMED);
        sendEmail(userEmail, deleteUnconfirmedSubject, deleteUnconfirmedContent);
    }

    public void sendEmail(String userEmail, String subject, String content) throws EmailException {
        try {
            MimeMessage message = new MimeMessage(prepareSession());
            message.setFrom(new InternetAddress(EmailConfig.getConfigProperty("host")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject(subject);
            message.setText(content, "UTF-8", "html");
            Transport.send(message);
        } catch (MessagingException e) {
            throw EmailException.emailNotSent(e);
        }
    }

    private Session prepareSession() {
        Properties sessionProperties = new Properties();
        sessionProperties.put("mail.smtp.host", EmailConfig.getConfigProperty("host"));
        sessionProperties.put("mail.smtp.port", EmailConfig.getConfigProperty("port"));
        sessionProperties.put("mail.smtp.auth", "true");
        sessionProperties.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(sessionProperties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EmailConfig.getConfigProperty("sender"),
                                EmailConfig.getConfigProperty("password"));
                    }
                });
    }
}
