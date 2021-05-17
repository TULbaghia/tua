package pl.lodz.p.it.ssbd2021.ssbd06.utils.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.PropertyReader;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.i18n.I18n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;

@ApplicationScoped
public class EmailConfig implements Serializable {

    @Inject
    private I18n i18n;

    private static final String MAIL_CONTENT_SCHEME = "mail.%s.content";
    private static final String MAIL_SUBJECT_SCHEME = "mail.%s.subject";

    private static final String MAIL_CONFIG_BUNDLE_NAME = "config";
    private static final String MAIL_CONFIG_PROPERTY_SCHEME = "mail.%s";

    public String getContentForType(String language, MailType type, String... param) {
        String mailType = String.format(MAIL_CONTENT_SCHEME, type.getValue());
        String pattern = i18n.getMessage(new Locale(language), mailType);
        return MessageFormat.format(pattern, (Object[]) param);
    }

    public String getSubjectForType(String language, MailType type) {
        String mailType = String.format(MAIL_SUBJECT_SCHEME, type.getValue());
        return i18n.getMessage(new Locale(language), mailType);
    }

    public String getConfigProperty(String param) {
        String property = String.format(MAIL_CONFIG_PROPERTY_SCHEME, param);
        return PropertyReader.getBundleProperty(MAIL_CONFIG_BUNDLE_NAME, property);
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
