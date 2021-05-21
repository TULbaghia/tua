package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppRuntimeException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.i18n.I18n;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

/**
 * Klasa reprezentująca dane konfiguracyjne aplikacji
 */
@ApplicationScoped
public class Config implements Serializable {

    @Inject
    private I18n i18n;

    private final Properties properties = new Properties();

    private static final String CONFIG_FILE = "config.properties";

    private static final String JWT_SECRET_KEY = "jwt.secretKey";
    private static final String JWT_EXPIRE_TIMEOUT = "jwt.expire";
    private static final String JWT_ISS = "jwt.iss";

    private static final String ETAG_SECRET_KEY = "etag.secretKey";

    private static final String RESET_EXPIRATION_MINUTES = "reset.expiration.minutes";

    private static final String MAIL_CONTENT_SCHEME = "mail.%s.content";
    private static final String MAIL_SUBJECT_SCHEME = "mail.%s.subject";

    private static final String MAIL_SENDER = "mail.sender";
    private static final String MAIL_HOST = "mail.host";
    private static final String MAIL_PORT = "mail.port";
    private static final String MAIL_PASSWORD = "mail.password";

    private String get(String key) {
        return properties.getProperty(key);
    }

    private int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    private long getLong(String key) {
        return Long.parseLong(get(key));
    }

    public String getJwtSecretKey() {
        return get(JWT_SECRET_KEY);
    }

    public Long getJwtExpireTimeout() {
        return getLong(JWT_EXPIRE_TIMEOUT);
    }

    public String getJwtIss() {
        return get(JWT_ISS);
    }

    public String getEtagSecretKey() {
        return get(ETAG_SECRET_KEY);
    }

    public int getResetExpirationMinutes() {
        return getInt(RESET_EXPIRATION_MINUTES);
    }

    public String getMailSender() {
        return get(MAIL_SENDER);
    }

    public String getMailHost() {
        return get(MAIL_HOST);
    }

    public String getMailPort() {
        return get(MAIL_PORT);
    }

    public String getMailPassword() {
        return get(MAIL_PASSWORD);
    }

    public String getContentForType(String language, Config.MailType type, String... param) {
        String mailType = String.format(MAIL_CONTENT_SCHEME, type.getValue());
        String pattern = i18n.getMessage(new Locale(language), mailType);
        return MessageFormat.format(pattern, (Object[]) param);
    }

    public String getSubjectForType(String language, Config.MailType type) {
        String mailType = String.format(MAIL_SUBJECT_SCHEME, type.getValue());
        return i18n.getMessage(new Locale(language), mailType);
    }

    @PostConstruct
    private void init() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (inputStream == null) {
                throw AppRuntimeException.configException();
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw AppRuntimeException.configException(e);
        }
    }

    @AllArgsConstructor
    public enum MailType {
        GRANT_ACCESS("access_grant"), DENY_ACCESS("access_deny"),
        LOCK_ACCOUNT("lock"), UNLOCK_ACCOUNT("unlock"),
        RESET_PASSWORD("password_reset"),
        DELETE_UNCONFIRMED("delete"),
        ACTIVATE_ACCOUNT("activate"),
        CHANGE_EMAIL("email_change");

        @Getter
        private final String value;
    }


}
