package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.CallingClass;

import javax.ejb.Local;
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
    HotelDto get(Long id) throws AppBaseException;

    /**
     * MOH.4 Zwraca listę hoteli
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista hoteli
     */
    List<HotelDto> getAll() throws AppBaseException;

    /**
     * MOH.6 Wyszukaj hotel
     *
     * @param option identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return dto hotelu
     */
    HotelDto lookForHotel(String ...option) throws AppBaseException;

    /**
     * MOH.7, MOH.28 Zwraca listę hoteli po przefiltrowaniu
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista hoteli
     */
    List<HotelDto> getAllFilter(String ...option) throws AppBaseException;

    /**
     * Dodaje hotel
     *
     * @param hotelDto dto z danymi miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void addHotel(NewHotelDto hotelDto) throws AppBaseException;

    /**
     * Modyfikuje hotel
     *
     * @param hotelDto dto z danymi hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void updateHotel(UpdateHotelDto hotelDto) throws AppBaseException;

    /**
     * Usuwa hotel
     *
     * @param hotelId identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void deleteHotel(Long hotelId) throws AppBaseException;

    /**
     * Przypisuje managera (po loginie) do hotelu
     *
     * @param hotelId identyfikator hotelu
     * @param managerLogin login managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void addManagerToHotel(Long hotelId, String managerLogin) throws AppBaseException;

    /**
     * Usuwa managera (po loginie) z hotelu
     *
     * @param managerLogin login managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void deleteManagerFromHotel(String managerLogin) throws AppBaseException;

    /**
     * Generuje raport nt. działalności hotelu
     *
     * @param hotelId identyfikator hotelu
     * @param from data od
     * @param to data do
     * @return Dane potrzebne do wygenerowania raportu
     */
    GenerateReportDto generateReport(Long hotelId, String from, String to) throws AppBaseException;
}
