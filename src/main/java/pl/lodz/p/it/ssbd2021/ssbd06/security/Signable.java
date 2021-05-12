package pl.lodz.p.it.ssbd2021.ssbd06.security;

import javax.json.bind.annotation.JsonbTransient;

/**
 * Interferjs odpowiedzialny za pobieranie niezbędnych danych do utworzenia nagłówka ETag
 */
public interface Signable {
    @JsonbTransient
    String getMessageToSign();
}
