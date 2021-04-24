package pl.lodz.p.it.ssbd2021.ssbd06.utils.email;

import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.Config;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Stateless
public class EmailSender {
    @Inject
    private Config config;

    @Asynchronous
    public void sendEmail(String recipientAddress, String mailSubject, String mailContent) throws MessagingException {
        try {
            MimeMessage message = new MimeMessage(prepareSession());
            message.setFrom(new InternetAddress(config.getMailSender()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress));
            message.setSubject(mailSubject);
            message.setText(mailContent, "UTF-8", "html");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new MessagingException(e.getMessage());
        }
    }

    private Session prepareSession() {
        Properties sessionProperties = new Properties();
        sessionProperties.put("mail.smtp.host", config.getMailHost());
        sessionProperties.put("mail.smtp.port", config.getMailPort());
        sessionProperties.put("mail.smtp.auth", "true");
        sessionProperties.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(sessionProperties,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getMailSender(), config.getMailPassword());
                }
        });
    }
}
