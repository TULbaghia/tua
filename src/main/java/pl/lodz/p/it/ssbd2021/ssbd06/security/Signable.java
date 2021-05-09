package pl.lodz.p.it.ssbd2021.ssbd06.security;

/**
 * Interferjs odpowiedzialny za pobieranie niezbędnych danych do utworzenia nagłówka ETag
 */
public interface Signable {
    String getMessageToSign();
}
