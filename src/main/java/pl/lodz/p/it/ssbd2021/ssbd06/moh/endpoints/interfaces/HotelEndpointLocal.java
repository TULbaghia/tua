package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CallingClass;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Login;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return dto hotelu
     */
    @PermitAll
    HotelDto get(Long id) throws AppBaseException;

    /**
     * MOH.4 Zwraca listę hoteli
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista hoteli
     */
    @PermitAll
    List<HotelDto> getAll() throws AppBaseException;

    /**
     * MOH.6 Wyszukaj hotel
     *
     * @param searchQuery identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return dto hotelu
     */
    @PermitAll
    List<HotelDto> lookForHotel(String searchQuery) throws AppBaseException;

    /**
     * MOH.7, MOH.28 Zwraca listę hoteli po przefiltrowaniu
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista hoteli
     */
    @PermitAll
    List<HotelDto> getAllFilter(String ...option) throws AppBaseException;

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
     * @param hotelId identyfikator hotelu
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
     * Generuje raport nt. działalności hotelu
     *
     * @param hotelId identyfikator hotelu
     * @param from data od
     * @param to data do
     * @return Dane potrzebne do wygenerowania raportu
     */
    @RolesAllowed("generateReport")
    GenerateReportDto generateReport(Long hotelId, String from, String to) throws AppBaseException;

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
     * @param id identyfikator hotelu.
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
}
