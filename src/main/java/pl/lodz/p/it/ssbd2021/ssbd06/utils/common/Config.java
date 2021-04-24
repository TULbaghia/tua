package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;
import java.util.ResourceBundle;


@ApplicationScoped
@Getter
public class Config implements Serializable {
    private static final String CONFIG_BUNDLE_NAME = "config";
    private static final String MAIL_HOST = "mail.host";
    private static final String MAIL_PORT = "mail.port";
    private static final String MAIL_SENDER = "mail.sender";
    private static final String MAIL_PASSWORD = "mail.password";

    @Getter(AccessLevel.NONE)
    private ResourceBundle resourceBundle;

    @PostConstruct
    private void init() {
        resourceBundle = ResourceBundle.getBundle(CONFIG_BUNDLE_NAME);
    }

    public String getProperty(String name) {
        return resourceBundle.getString(name);
    }

    public String getMailHost() {
        return getProperty(MAIL_HOST);
    }

    public String getMailPort() {
        return getProperty(MAIL_PORT);
    }

    public String getMailSender() {
        return getProperty(MAIL_SENDER);
    }

    public String getMailPassword() {
        return getProperty(MAIL_PASSWORD);
    }
}
