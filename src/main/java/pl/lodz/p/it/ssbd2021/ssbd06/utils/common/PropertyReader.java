package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class PropertyReader {

    public static String getBundleProperty(String resourceBundleName, String propertyName, String firstParam, String secondParam) {
        String pattern = getBundle(resourceBundleName).getString(propertyName);
        return MessageFormat.format(pattern, firstParam, secondParam);
    }

    public static String getBundleProperty(String resourceBundleName, String propertyName, String param) {
        String pattern = getBundle(resourceBundleName).getString(propertyName);
        return MessageFormat.format(pattern, param);
    }

    public static String getBundleProperty(String resourceBundleName, String propertyName) {
        return getBundle(resourceBundleName).getString(propertyName);
    }

    public static ResourceBundle getBundle(String resourceBundleName) {
        return ResourceBundle.getBundle(resourceBundleName);
    }
}
