package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.ReportRowDto;

import java.util.Date;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy Hotel na klasy DTO, oraz z obiektów klas DTO na obiekty klasy Hotel.
 */
@Mapper
public interface IBookingMapper {
    /**
     * Dokonuje mapowania z obiektu Booking na obiekt ReportRowDto.
     *
     * @param booking obiekt klasy hotel.
     * @return zmapowany obiekt klasy ReportRowDto.
     */
    @Mapping(target = "ownerLogin", expression = "java(booking.getAccount().getLogin())")
    ReportRowDto toReportRowDto(Booking booking);

    default Long map(Date value) {
        return value == null ? null : value.getTime();
    }

    default Short map(Rating value) {
        return value == null ? null : value.getRate();
    }
}
