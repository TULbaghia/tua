package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * Klasa DTO reprezentujaca rezerwację z dodatkowymi informacjami
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailBookingDto implements Signable {
    private Long id;

    private DetailBooking_Renter renter;

    private Date creationDate;

    private Date modificationDate;
    private Date dateFrom;
    private Date dateTo;

    private String bookingStatus;
    private BigDecimal price;

    private Set<DetailBooking_LineDto> bookingLine;

    private Long ratingId;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d;%d", id, renter.id, version);
    }

    @Data
    @AllArgsConstructor
    public static class DetailBooking_LineDto {
        private Long id;
        private Long boxId;
        private BigDecimal pricePerDay;
    }

    @Data
    @AllArgsConstructor
    public static class DetailBooking_Renter {
        private Long id;
        private String login;
        private String firstname;
        private String lastname;
        private String email;
        private String contactNumber;
        private boolean active;
    }
}
