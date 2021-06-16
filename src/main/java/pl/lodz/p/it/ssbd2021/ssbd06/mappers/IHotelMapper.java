package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateHotelDto;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy Hotel na klasy DTO, oraz z obiektów klas DTO na obiekty klasy Hotel
 */
@Mapper
public interface IHotelMapper {
    /**
     * Dokonuje mapowania z obiektu Hotel na obiekt HotelDto
     * @param hotel obiekt klasy Hotel
     * @return zmapowany obiekt klasy UpdateHotelDto
     */
    @Mapping(target = "cityName", expression = "java(hotel.getCity().getName())")
    HotelDto toHotelDto(Hotel hotel);

    /**
     * Dokonuje mapowania z obiektu UpdateHotelDto na obiekt Hotel
     * @param updateHotelDto obiekt klasy UpdateHotelDto
     * @param hotel obiekt klasy Hotel na który przeprowadzamy mapowanie
     * @throws AppBaseException gdy nie udało się przeprowadzić operacji mapowania
     */
    void toHotel(UpdateHotelDto updateHotelDto, @MappingTarget Hotel hotel) throws AppBaseException;
}
