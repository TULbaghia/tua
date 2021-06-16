package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;

import java.math.BigDecimal;

/**
 * Klasa DTO reprezentująca klatkę
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxDto implements Signable {
    private Long id;
    private BigDecimal price;
    private String description;
    private String animalType;
    private Long hotelId;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d;%s", id, hotelId, animalType);
    }
}
