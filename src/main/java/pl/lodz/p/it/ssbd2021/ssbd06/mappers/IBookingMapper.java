package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.BookingLine;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.DetailBookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.DetailBookingDto.DetailBooking_LineDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.ReportRowDto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy Booking na klasy DTO, oraz z obiektów klas DTO na obiekty
 * klasy Booking
 */
@Mapper
public interface IBookingMapper {
    /**
     * Dokonuje mapowania z obiektu Booking na obiekt BookingDto
     *
     * @param booking obiekt klasy Booking
     * @return zmapowany obiekt klasy BookingDto
     */
    BookingDto toBookingDto(Booking booking);

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

    /**
     * Dokonuje mapowania z obiektu Booking na obiekt BookingDto
     *
     * @param booking obiekt klasy Booking
     * @return zmapowany obiekt klasy BookingDto
     */
    @Mappings({
            @Mapping(target = "renterLogin", source = "account.login"),
            @Mapping(target = "renterId", source = "account.id"),
            @Mapping(target = "bookingStatus", source = "status"),
            @Mapping(target = "bookingLine", source = "bookingLineList", qualifiedByName = {"mapBookingLineToDto"})
    })
    DetailBookingDto toDetailBookingDto(Booking booking);

    /**
     * Dokonuje mapowania z obiektu Set<BookingLine> na obiekt Set<DetailBooking_LineDto>
     *
     * @param bookingLines set obiektów klasy BookingLine
     * @return zmapowany set obiekt klasy DetailBooking_LineDto
     */
    @Named("mapBookingLineToDto")
    default Set<DetailBooking_LineDto> mapToSetLineDto(List<BookingLine> bookingLines) {
        Set<DetailBooking_LineDto> detailBookingLineDtos = new HashSet<>();
        bookingLines.forEach(x -> {
            detailBookingLineDtos.add(Mappers.getMapper(IBookingMapper.class).toDetailBookingLineDto(x));
        });
        return detailBookingLineDtos;
    }

    /**
     * Dokonuje mapowania z obiektu BookingLine na obiekt DetailBooking_LineDto
     *
     * @param bookingLine obiekt klasy BookingLine
     * @return zmapowany obiekt klasy DetailBooking_LineDto
     */
    @Mappings({
            @Mapping(target = "boxId", source = "box.id")
    })
    DetailBooking_LineDto toDetailBookingLineDto(BookingLine bookingLine);
}