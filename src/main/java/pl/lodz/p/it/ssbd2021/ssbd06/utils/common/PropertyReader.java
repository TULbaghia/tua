package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import java.util.ResourceBundle;

public class PropertyReader {

    public static String getBundleProperty(String resourceBundleName, String propertyName) {
        return getBundle(resourceBundleName).getString(propertyName);
    }

    public static ResourceBundle getBundle(String resourceBundleName) {
        return ResourceBundle.getBundle(resourceBundleName);
    }
}
