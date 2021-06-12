package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BoxDto;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy Box na klasy DTO
 * oraz z obiektów klas DTO na obiekty klasy Box
 */
@Mapper
public interface IBoxMapper {
    /**
     * Mapuje obiekt klasy Box na obiekt klasy BoxDto
     * @param box obiekt klasy Box
     * @return zmapowany obiekt klasy BoxDto
     */
    @Mappings({
            @Mapping(target = "hotelId", source = "hotel.id"),
            @Mapping(target = "price", source = "pricePerDay")
    })
    BoxDto toBoxDto(Box box);
}
