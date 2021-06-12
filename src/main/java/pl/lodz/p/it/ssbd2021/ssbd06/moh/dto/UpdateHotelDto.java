package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;

/**
 * Klasa DTO reprezentujaca aktualizację hotelu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHotelDto implements Signable {
    private Long id;
    private String name;
    private String address;
    private Long cityId;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d", id, version);
    }
}
