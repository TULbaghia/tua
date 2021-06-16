package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.*;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CallingClass;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie klatkami.
 */
@Local
public interface BoxEndpointLocal extends CallingClass {
    /**
     * Zwraca klatke o podanym identyfikatorze
     *
     * @param id identyfikator klatki
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return dto klatki
     */
    BoxDto get(Long id) throws AppBaseException;

    /**
     * Zwraca listę klatek
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista klatek
     */
    @RolesAllowed("getAllBoxes")
    List<BoxDto> getAll() throws AppBaseException;

    /**
     * @param hotelId identyfikator hotelu dla którego chcemy zwrócić listę klatek
     * @return lista klatek
     * @throws AppBaseException w momencie wystąpienia błędu
     */
    @RolesAllowed("getAllBoxes")
    List<BoxDto> getAllBoxesInHotel(Long hotelId) throws AppBaseException;

    /**
     * @param hotelId identyfikator hotelu dla którego chcemy zwrócić listę klatek
     * @return lista klatek
     * @throws AppBaseException w momencie wystąpienia błędu
     */
    @RolesAllowed("getAllBoxes")
    List<BoxDto> getSomeTypeBoxesFromHotel(Long hotelId, AnimalType animalType) throws AppBaseException;

    /**
     * Dodaje klatkę
     *
     * @param boxDto dto z danymi nowej klatki
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addBox")
    void addBox(NewBoxDto boxDto) throws AppBaseException;

    /**
     * Modyfikuje klatkę
     *
     * @param boxDto dto z danymi klatki
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("updateBox")
    void updateBox(UpdateBoxDto boxDto) throws AppBaseException;

    /**
     * Usuwa klatkę
     *
     * @param boxId identyfikator klatki
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteBox")
    void deleteBox(Long boxId) throws AppBaseException;
}
