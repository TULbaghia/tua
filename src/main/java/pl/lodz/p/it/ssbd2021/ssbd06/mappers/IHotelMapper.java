package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy Hotel na klasy DTO, oraz z obiektów klas DTO na obiekty klasy Hotel
 */
@Mapper
public interface IHotelMapper {

    /**
     * Dokonuje mapowania z obiektu Hotel na obiekt HotelDto
     * @param hotel obiekt klasy Hotel
     * @return zmapowany obiekt klasy HotelDto
     */
    HotelDto toHotelDto(Hotel hotel);
}
