package pl.lodz.p.it.ssbd2021.ssbd06.security;

import javax.json.bind.annotation.JsonbTransient;

/**
 * Interferjs odpowiedzialny za pobieranie niezbędnych danych do utworzenia nagłówka ETag
 */
public interface Signable {
    /**
     * Pobiera dane niezbędne do utworzenia nagłówka ETag
     * @return dane do utworzenia nagłówka ETag
     */
    @JsonbTransient
    String getMessageToSign();
}
