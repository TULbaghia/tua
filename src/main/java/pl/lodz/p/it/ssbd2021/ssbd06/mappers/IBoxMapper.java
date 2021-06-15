package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBoxDto;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy Box na klasy DTO
 * oraz z obiektów klas DTO na obiekty klasy Box
 */
@Mapper
public interface IBoxMapper {
    /**
     * Mapuje obiekt klasy Box na obiekt klasy BoxDto
     *
     * @param box obiekt klasy Box
     * @return zmapowany obiekt klasy BoxDto
     */
    @Mappings({
            @Mapping(target = "hotelId", source = "hotel.id"),
            @Mapping(target = "price", source = "pricePerDay")
    })
    BoxDto toBoxDto(Box box);

    /**
     * Mapuje obiekt klasy NewBoxDto na obiekt klasy Box
     *
     * @param newBoxDto obiekt klasy NewBoxDto
     * @param box obiekt klasy Box
     * @throws AppBaseException w sytuacji wystąpienia błędu
     */
    @Mappings({
            @Mapping(target = "pricePerDay", source = "price")
    })
    void toBox(NewBoxDto newBoxDto, @MappingTarget Box box) throws AppBaseException;


    /**
     * Mapuje obiekt klasy BoxDto na obiekt klasy Box
     *
     * @param boxDto obiekt klasy BoxDto
     * @param box obiekt klasy Box
     * @throws AppBaseException w sytuacji wystąpienia błędu
     */
    @Mappings({
            @Mapping(target = "pricePerDay", source = "price")
    })
    void toBox(BoxDto boxDto, @MappingTarget Box box) throws AppBaseException;
}
