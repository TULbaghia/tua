package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;

@ApplicationScoped
@Getter
public class EmailConfig implements Serializable {

    // Application properties bundle
    private static final String CONFIG_BUNDLE_NAME = "config";
    private static final String MAIL_HOST = "mail.host";
    private static final String MAIL_PORT = "mail.port";
    private static final String MAIL_SENDER = "mail.sender";
    private static final String MAIL_PASSWORD = "mail.password";
    // Email resources bundle
    private static final String MAIL_BUNDLE_NAME = "emailResources";
    private static final String ACTIVATION_MAIL_SUBJECT = "mail.activation.subject";
    private static final String ACTIVATION_MAIL_CONTENT = "mail.activation.content";

    public String getMailHost() {
        return PropertyReader.getBundleProperty(CONFIG_BUNDLE_NAME, MAIL_HOST);
    }

    public String getMailPort() {
        return PropertyReader.getBundleProperty(CONFIG_BUNDLE_NAME, MAIL_PORT);
    }

    public String getMailSender() {
        return PropertyReader.getBundleProperty(CONFIG_BUNDLE_NAME, MAIL_SENDER);
    }

    public String getMailPassword() {
        return PropertyReader.getBundleProperty(CONFIG_BUNDLE_NAME, MAIL_PASSWORD);
    }

    public String getActivationMailSubject() {
        return PropertyReader.getBundleProperty(MAIL_BUNDLE_NAME, ACTIVATION_MAIL_SUBJECT);
    }

    public String getActivationMailContent(String username, String activationLink) {
        return PropertyReader.getBundleProperty(MAIL_BUNDLE_NAME, ACTIVATION_MAIL_CONTENT, username, activationLink);
    }
}
