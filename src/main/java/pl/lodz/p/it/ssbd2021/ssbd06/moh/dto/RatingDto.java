package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;

/**
 * Klasa DTO reprezentująca ocenę hotelu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto implements Signable {
    private Long id;
    private short rate;
    private String comment;
    private Long bookingId;
    private boolean hidden;
    private String createdBy;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%s;%d", id, createdBy, version);
    }
}
