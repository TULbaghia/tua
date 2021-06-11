package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BookingDto;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy Booking na klasy DTO, oraz z obiektów klas DTO na obiekty klasy Booking
 */
@Mapper
public interface IBookingMapper {
    /**
     * Dokonuje mapowania z obiektu Booking na obiekt BookingDto
     * @param booking obiekt klasy Booking
     * @return zmapowany obiekt klasy BookingDto
     */
    BookingDto toBookingDto(Booking booking);
}
