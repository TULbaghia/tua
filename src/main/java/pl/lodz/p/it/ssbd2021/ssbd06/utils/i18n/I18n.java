package pl.lodz.p.it.ssbd2021.ssbd06.utils.i18n;

import lombok.extern.java.Log;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Klasa przeznaczona do internacjonalizacji komunikatów
 */
@Log
@RequestScoped
public class I18n {
    private static String BUNDLE_MESSAGES = "messages";

    @Inject
    private HttpServletRequest servletRequest;

    /**
     * Tłumaczy klucz na wiadomość w języku pobranym z kontenera
     *
     * @param key klucz wiadomości
     * @return znajdująca się pod danym kluczem
     */
    public String getMessage(String key) {
        try {
            return getMessage(servletRequest.getLocale(), key);
        } catch (NullPointerException e) {
            log.warning("Exception during translation of '" + key + "' :: " + e.getMessage());
        }
        return key;
    }

    /**
     * Tłumaczy klucz na wiadomość w języku przekazanym jako parametr
     *
     * @param locale język tłumaczenia
     * @param key klucz wiadomości
     * @return znajdująca się pod danym kluczem
     */
    public String getMessage(Locale locale, String key) {
        try {
            return getBundle(locale).getString(key);
        } catch (NullPointerException e) {
            log.warning("Exception during translation of '" + key + "' :: " + e.getMessage());
        }
        return key;
    }

    /**
     * Tworzy paczkę językową dla przekazanego języka
     *
     * @param locale język tłumaczenia
     * @return paczka językowa
     */
    private ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_MESSAGES, locale);
    }

}
