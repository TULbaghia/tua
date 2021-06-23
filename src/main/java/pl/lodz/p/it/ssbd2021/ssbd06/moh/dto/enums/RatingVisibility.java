package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Typ wyliczeniowy określający poziomy widoczności komentarza.
 */
@Getter
@AllArgsConstructor
public enum RatingVisibility {
    VISIBLE(true),
    HIDDEN(false);

    private final boolean value;
}
