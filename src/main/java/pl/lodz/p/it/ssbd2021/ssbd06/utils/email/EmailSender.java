package pl.lodz.p.it.ssbd2021.ssbd06.utils.email;

import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.EmailConfig;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Stateless
public class EmailSender {

    @Inject
    private EmailConfig emailConfig;

    public void sendActivationEmail(String userName, String userEmail, String activationLink) throws MessagingException {
        String activationContent = emailConfig.getActivationMailContent(userName, activationLink);
        String activationSubject = emailConfig.getActivationMailSubject();
        sendEmail(userEmail, activationSubject, activationContent);
    }

    public void sendEmail(String userEmail, String subject, String content) throws MessagingException {
        try {
            MimeMessage message = new MimeMessage(prepareSession());
            message.setFrom(new InternetAddress(emailConfig.getMailSender()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject(subject);
            message.setText(content, "UTF-8", "html");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new MessagingException(e.getMessage());
        }
    }

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
                    return new PasswordAuthentication(emailConfig.getMailSender(), emailConfig.getMailPassword());
                }
        });
    }
}
