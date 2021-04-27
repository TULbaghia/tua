package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.text.MessageFormat;

public class EmailConfig implements Serializable {

    private static final String MAIL_BUNDLE_NAME = "emailResources";
    private static final String MAIL_CONTENT_SCHEME = "mail.%s.content";
    private static final String MAIL_SUBJECT_SCHEME = "mail.%s.subject";

    private static final String MAIL_CONFIG_BUNDLE_NAME = "config";
    private static final String MAIL_CONFIG_PROPERTY_SCHEME = "mail.%s";

    public static String getContentForType(MailType type, String... param) {
        String mailType = String.format(MAIL_CONTENT_SCHEME, type.getValue());
        String pattern = PropertyReader.getBundleProperty(MAIL_BUNDLE_NAME, mailType);
        return MessageFormat.format(pattern, (Object[]) param);
    }

    public static String getSubjectForType(MailType type) {
        String mailType = String.format(MAIL_SUBJECT_SCHEME, type.getValue());
        return PropertyReader.getBundleProperty(MAIL_BUNDLE_NAME, mailType);
    }

    public static String getConfigProperty(String param) {
        String property = String.format(MAIL_CONFIG_PROPERTY_SCHEME, param);
        return PropertyReader.getBundleProperty(MAIL_CONFIG_BUNDLE_NAME, property);
    }

    @AllArgsConstructor
    public enum MailType {
        GRANT_ACCESS("grantaccess"), DENY_ACCESS("denyaccess"),
        LOCK_ACCOUNT("lock"), UNLOCK_ACCOUNT("unlock"),
        RESET_PASSWORD("passwordreset"),
        DELETE_UNCONFIRMED("delete"),
        ACTIVATE_ACCOUNT("activate");

        @Getter
        private final String value;
    }
}
