package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateHotelDto;

import javax.ws.rs.*;
import java.util.List;

/**
 * Kontroler odpowiadający za zarządzanie hotelami.
 */
@Path("/hotels")
public class HotelController extends AbstractController {
    /**
     * Zwraca hotel o podanym identyfikatorze
     *
     * @param id identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z pobieraniem hotelu
     * @return dto hotelu
     */
    @GET
    @Path("/{id}")
    public HotelDto get(@PathParam("id") Long id) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę hoteli
     *
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy hoteli
     * @return lista dto hoteli
     */
    @GET
    public List<HotelDto> getAll() throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Wyszukaj hotel
     *
     * @param option opcje hotelu
     * @throws AppBaseException podczas błędu związanego z wyszukiwaniem hotelu
     * @return dto hotelu
     */
    @GET
    @Path("/look/{option}")
    public HotelDto lookForHotel(@PathParam("option") String option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę hoteli po przefiltrowaniu
     *
     * @throws AppBaseException podczas błędu związanego z listą hoteli
     * @return lista dto hoteli
     */
    @GET
    @Path("/filter/{option}")
    public List<HotelDto> getAllFilter(@PathParam("option") String option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje hotel
     *
     * @param hotelDto dto z danymi hotelu
     * @throws AppBaseException podczas błędu związanego z dodawaniem hotelu
     */
    @POST
    public void addHotel(NewHotelDto hotelDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Modyfikuje hotel
     *
     * @param hotelDto dto z danymi hotelu
     * @throws AppBaseException podczas błędu związanego z aktualizacją hotelu
     */
    @PUT
    public void updateHotel(UpdateHotelDto hotelDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Usuwa hotel
     *
     * @param hotelId identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z usuwaniem hotelu
     */
    @DELETE
    public void deleteHotel(Long hotelId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Przypisuje managera (po loginie) do hotelu
     *
     * @param hotelId identyfikator hotelu
     * @param managerLogin login managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z przypisywaniem managera do hotelu
     */
    @PATCH
    @Path("/add/{managerLogin}/{hotelId}")
    public void addManagerToHotel(@PathParam("hotelId") Long hotelId, @PathParam("managerLogin") String managerLogin) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Usuwa managera (po loginie) z hotelu
     *
     * @param managerLogin login managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z usunięciem managera z hotelu
     */
    @PATCH
    @Path("/remove/{managerLogin}")
    public void deleteManagerFromHotel(@PathParam("managerLogin") String managerLogin) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Generuje raport nt. działalności hotelu
     *
     * @param hotelId identyfikator hotelu
     * @param from data od
     * @param to data do
     * @return dane potrzebne do wygenerowania raportu
     */
    @GET
    @Path("/raport/{hotelId}/{from}/{to}")
    public GenerateReportDto generateReport(@PathParam("hotelId") Long hotelId, @PathParam("from") String from, @PathParam("to") String to) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
