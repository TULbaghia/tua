package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Klasa DTO reprezentujaca rezerwację
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto implements Signable {
    private Long id;

    private Long accountId;

    private Date dateFrom;
    private Date dateTo;

    private String bookingStatus;
    private BigDecimal price;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d", id, version);
    }
}
