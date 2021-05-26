package pl.lodz.p.it.ssbd2021.ssbd06.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ThemeColor {
    LIGHT("LIGHT"),
    DARK("DARK");

    private final String value;
}
