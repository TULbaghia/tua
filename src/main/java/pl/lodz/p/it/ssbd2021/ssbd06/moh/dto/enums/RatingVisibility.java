package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum definiujące widoczoność komentarzy
 */
@Getter
@AllArgsConstructor
public enum RatingVisibility {
    VISIBLE(true),
    HIDDEN(false);

    private final boolean value;
}
