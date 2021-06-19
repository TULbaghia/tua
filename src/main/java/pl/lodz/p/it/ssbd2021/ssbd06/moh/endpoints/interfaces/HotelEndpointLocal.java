package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CallingClass;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.math.BigDecimal;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie hotelami.
 */
@Local
public interface HotelEndpointLocal extends CallingClass {
    /**
     * MOH.5 Zwraca hotel o podanym identyfikatorze
     *
     * @param id identyfikator hotelu
     * @return dto hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    HotelDto get(Long id) throws AppBaseException;

    /**
     * MOH.4 Zwraca listę hoteli
     *
     * @return lista hoteli
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    List<HotelDto> getAll() throws AppBaseException;

    /**
     * MOH.6 Wyszukaj hotel
     *
     * @param searchQuery identyfikator hotelu
     * @return dto hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    List<HotelDto> lookForHotel(String searchQuery) throws AppBaseException;

    /**
     * MOH.7, MOH.28 Zwraca listę hoteli po przefiltrowaniu
     * @param fromRating dolny przedział oceny hotelu
     * @param toRating górny przedział oceny hotelu
     * @param dog wartość logiczna określająca AnimalType.DOG
     * @param cat wartość logiczna określająca AnimalType.CAT
     * @param rodent wartość logiczna określająca AnimalType.RODENT
     * @param bird wartość logiczna określająca AnimalType.BIRD
     * @param rabbit wartość logiczna określająca AnimalType.RABBIT
     * @param lizard wartość logiczna określająca AnimalType.LIZARD
     * @param turtle wartość logiczna określająca AnimalType.TURTLE
     * @return
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @PermitAll
    List<HotelDto> getAllFilter(BigDecimal fromRating,
                                BigDecimal toRating,
                                boolean dog,
                                boolean cat,
                                boolean rodent,
                                boolean bird,
                                boolean rabbit,
                                boolean lizard,
                                boolean turtle) throws AppBaseException;

    /**
     * Dodaje hotel
     *
     * @param hotelDto dto z danymi miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addHotel")
    void addHotel(NewHotelDto hotelDto) throws AppBaseException;

    /**
     * Usuwa hotel
     *
     * @param hotelId identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteHotel")
    void deleteHotel(Long hotelId) throws AppBaseException;

    /**
     * Przypisuje managera (po loginie) do hotelu
     *
     * @param hotelId      identyfikator hotelu
     * @param managerLogin login managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addManagerToHotel")
    void addManagerToHotel(Long hotelId, String managerLogin) throws AppBaseException;

    /**
     * Usuwa managera (po loginie) z hotelu
     *
     * @param managerLogin login managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteManagerFromHotel")
    void deleteManagerFromHotel(String managerLogin) throws AppBaseException;

    /**
     * Generuje raport nt. działalności hotelu z zadanego okresu.
     *
     * @param from data od (dla generowanego raportu)
     * @param to   data do (dla generowanego raportu)
     * @return dane potrzebne do wygenerowania raportu
     */
    @RolesAllowed("generateReport")
    GenerateReportDto generateReport(Long from, Long to) throws AppBaseException;

    /**
     * Modyfikuje hotel managera.
     *
     * @param hotelDto dto z danymi hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("updateOwnHotel")
    void updateOwnHotel(UpdateHotelDto hotelDto) throws AppBaseException;

    /**
     * Modyfikuje dowolny hotel.
     *
     * @param id       identyfikator hotelu.
     * @param hotelDto dto z danymi hotelu.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @RolesAllowed("updateOtherHotel")
    void updateOtherHotel(Long id, UpdateHotelDto hotelDto) throws AppBaseException;

    /**
     * Zwraca dane hotelu przypisanego do managera.
     *
     * @return obiekt transferowy z danymi hotelu
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @RolesAllowed("getOwnHotelInfo")
    HotelDto getOwnHotelInfo() throws AppBaseException;

    /**
     * Zwraca dane hotelu o podanym id.
     *
     * @param id identyfikator hotelu
     * @return obiekt transferowy z danymi hotelu
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @RolesAllowed("getOtherHotelInfo")
    HotelDto getOtherHotelInfo(Long id) throws AppBaseException;

    /**
     * Zwraca dane hotelu dla rezerwacji o danym id.
     *
     * @param id id rezerwacji
     * @return obiekt transferowy z danymi hotelu
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @RolesAllowed("getHotelForBooking")
    HotelDto getHotelForBooking(Long id) throws AppBaseException;
}
