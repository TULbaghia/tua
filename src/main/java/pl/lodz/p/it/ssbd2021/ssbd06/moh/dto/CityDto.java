package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.CityDescription;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.moh.CityName;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO reprezentująca miasto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto implements Signable {
    @NotNull
    private Long id;

    @NotNull
    @CityName
    private String name;

    @NotNull
    @CityDescription
    private String description;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d", id, version);
    }
}
