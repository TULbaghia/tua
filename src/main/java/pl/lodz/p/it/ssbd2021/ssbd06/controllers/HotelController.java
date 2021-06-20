package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import org.eclipse.microprofile.openapi.annotations.Operation;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.HotelEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.security.EtagValidatorFilterBinding;
import pl.lodz.p.it.ssbd2021.ssbd06.security.MessageSigner;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.HotelEndpoint;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

/**
 * Kontroler odpowiadający za zarządzanie hotelami.
 */
@Path("/hotels")
public class HotelController extends AbstractController {

    @Inject
    private HotelEndpointLocal hotelEndpoint;

    @Inject
    private MessageSigner messageSigner;

    /**
     * Zwraca hotel o podanym identyfikatorze
     *
     * @param id identyfikator hotelu
     * @return dto hotelu
     * @throws AppBaseException podczas błędu związanego z pobieraniem hotelu
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getHotel", summary = "getHotel")
    public HotelDto get(@PathParam("id") Long id) throws AppBaseException {
        return repeat(() -> hotelEndpoint.get(id), hotelEndpoint);
    }

    /**
     * Zwraca listę hoteli
     *
     * @return lista dto hoteli
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy hoteli
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getAllHotelsList", summary = "getAllHotelsList")
    public List<HotelDto> getAll() throws AppBaseException {
        return repeat(() -> hotelEndpoint.getAll(), hotelEndpoint);
    }

    /**
     * Wyszukaj hotel
     *
     * @param option opcje hotelu
     * @return dto hotelu
     * @throws AppBaseException podczas błędu związanego z wyszukiwaniem hotelu
     */
    @GET
    @Path("/look/{option}")
    @Operation(operationId = "lookForHotel", summary = "lookForHotel")
    public List<HotelDto> lookForHotel(@PathParam("option") String option) throws AppBaseException {
        return repeat(() -> hotelEndpoint.lookForHotel(option), hotelEndpoint);
    }

    /**
     * Zwraca listę hoteli po przefiltrowaniu
     *
     * @return lista dto hoteli
     * @throws AppBaseException podczas błędu związanego z listą hoteli
     */
    @GET
    @Path("/filter/{option}")
    @Operation(operationId = "filterAllHotels", summary = "filterAllHotels")
    public List<HotelDto> getAllFilter(@PathParam("option") String option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę hoteli po przefiltrowaniu
     * @param fromRating dolny przedział oceny hotelu
     * @param toRating górny przedział oceny hotelu
     * @param dogType wartość logiczna określająca czy hotel przyjmuje psy
     * @param catType wartość logiczna określająca czy hotel przyjmuje koty
     * @param rodentType wartość logiczna określająca czy hotel przyjmuje gryzonie
     * @param birdType wartość logiczna określająca czy hotel przyjmuje ptaki
     * @param rabbitType wartość logiczna określająca czy hotel przyjmuje króliki
     * @param lizardType wartość logiczna określająca czy hotel przyjmuje jaszczurki
     * @param turtleType wartość logiczna określająca czy hotel przyjmuje żółwie
     * @return lista hoteli
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @GET
    @Path("/filter")
    public List<HotelDto> getFilteredHotels(@QueryParam(value = "fromRating") Double fromRating,
                                            @QueryParam(value = "toRating") Double toRating,
                                            @QueryParam(value = "dogType") String dogType,
                                            @QueryParam(value = "catType") String catType,
                                            @QueryParam(value = "rodentType") String rodentType,
                                            @QueryParam(value = "birdType") String birdType,
                                            @QueryParam(value = "rabbitType") String rabbitType,
                                            @QueryParam(value = "lizardType") String lizardType,
                                            @QueryParam(value = "turtleType") String turtleType,
                                            @QueryParam(value = "searchQuery") String searchQuery) throws AppBaseException {
        BigDecimal fromValue = BigDecimal.valueOf(Objects.requireNonNullElse(fromRating, 1.0));
        BigDecimal toValue = BigDecimal.valueOf(Objects.requireNonNullElse(toRating, 5.0));
        boolean isDog = Boolean.parseBoolean(Objects.requireNonNullElse(dogType, "false"));
        boolean isCat = Boolean.parseBoolean(Objects.requireNonNullElse(catType, "false"));
        boolean isRodent = Boolean.parseBoolean(Objects.requireNonNullElse(rodentType, "false"));
        boolean isBird = Boolean.parseBoolean(Objects.requireNonNullElse(birdType, "false"));
        boolean isRabbit = Boolean.parseBoolean(Objects.requireNonNullElse(rabbitType, "false"));
        boolean isLizard = Boolean.parseBoolean(Objects.requireNonNullElse(lizardType, "false"));
        boolean isTurtle = Boolean.parseBoolean(Objects.requireNonNullElse(turtleType, "false"));
        String searchText = Objects.requireNonNullElse(searchQuery, "");
        return repeat(() -> hotelEndpoint.getAllFilter(fromValue, toValue, isDog, isCat, isRodent, isBird, isRabbit, isLizard, isTurtle, searchText), hotelEndpoint);
    }

    /**
     * Dodaje hotel
     *
     * @param hotelDto dto z danymi hotelu
     * @throws AppBaseException podczas błędu związanego z dodawaniem hotelu
     */
    @POST
    @RolesAllowed("addHotel")
    @Operation(operationId = "addHotel", summary = "addHotel")
    public void addHotel(@Valid NewHotelDto hotelDto) throws AppBaseException {
        repeat(() -> hotelEndpoint.addHotel(hotelDto), hotelEndpoint);
    }

    /**
     * Usuwa hotel
     *
     * @param hotelId identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z usuwaniem hotelu
     */
    @DELETE
    @RolesAllowed("deleteHotel")
    @Operation(operationId = "deleteHotel", summary = "deleteHotel")
    public void deleteHotel(Long hotelId) throws AppBaseException {
        repeat(() -> hotelEndpoint.deleteHotel(hotelId), hotelEndpoint);
    }

    /**
     * Przypisuje managera (po loginie) do hotelu
     *
     * @param hotelId      identyfikator hotelu
     * @param managerLogin login managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z przypisywaniem managera do hotelu
     */
    @PATCH
    @RolesAllowed("addManagerToHotel")
    @Path("/add/{managerLogin}/{hotelId}")
    @Operation(operationId = "addManagerToHotel", summary = "addManagerToHotel")
    public void addManagerToHotel(@PathParam("hotelId") Long hotelId, @PathParam("managerLogin") String managerLogin)
            throws AppBaseException {
        repeat(() -> hotelEndpoint.addManagerToHotel(hotelId, managerLogin), hotelEndpoint);
    }

    /**
     * Usuwa managera (po loginie) z hotelu
     *
     * @param managerLogin login managera do usunięcia z hotelu
     * @throws AppBaseException podczas błędu związanego z usunięciem managera z hotelu
     */
    @PATCH
    @RolesAllowed("deleteManagerFromHotel")
    @EtagValidatorFilterBinding
    @Path("/remove/{managerLogin}")
    @Operation(operationId = "deleteManagerFromHotel", summary = "deleteManagerFromHotel")
    public void deleteManagerFromHotel(@PathParam("managerLogin") String managerLogin) throws AppBaseException {
        repeat(() -> hotelEndpoint.deleteManagerFromHotel(managerLogin), hotelEndpoint);
    }

    /**
     * Modyfikuje hotel managera.
     *
     * @param hotelDto dto z danymi hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PUT
    @Path("/edit")
    @RolesAllowed("updateOwnHotel")
    @EtagValidatorFilterBinding
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "updateOwnHotel", summary = "updateOwnHotel")
    public void updateOwnHotel(@Valid UpdateHotelDto hotelDto) throws AppBaseException {
        repeat(() -> hotelEndpoint.updateOwnHotel(hotelDto), hotelEndpoint);
    }


    /**
     * Modyfikuje dowolny hotel.
     *
     * @param id       identyfikator hotelu.
     * @param hotelDto dto z danymi hotelu.
     * @throws AppBaseException podczas błędu związanego z bazą danych.
     */
    @PUT
    @Path("/edit/{id}")
    @RolesAllowed("updateOtherHotel")
    @EtagValidatorFilterBinding
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "updateOtherHotel", summary = "updateOtherHotel")
    public void updateOtherHotel(@PathParam("id") Long id, @Valid UpdateHotelDto hotelDto) throws AppBaseException {
        repeat(() -> hotelEndpoint.updateOtherHotel(id, hotelDto), hotelEndpoint);
    }

    /**
     * Zwraca dane konkretnego użytkownika.
     *
     * @return odpowiedź z danymi hotelu i wartością Etag.
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych.
     */
    @GET
    @RolesAllowed("getOwnHotelInfo")
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getOwnHotelInfo", summary = "getOwnHotelInfo")
    public Response getOwnHotelInfo() throws AppBaseException {
        HotelDto hotelDto = repeat(() -> hotelEndpoint.getOwnHotelInfo(), hotelEndpoint);
        return Response.ok()
                .entity(hotelDto)
                .header("ETag", messageSigner.sign(hotelDto))
                .build();
    }

    /**
     * Zwraca dane konkretnego użytkownika.
     *
     * @param id identyfikator hotelu.
     * @return odpowiedź z danymi hotelu i wartością Etag.
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych.
     */
    @GET
    @RolesAllowed("getOtherHotelInfo")
    @Path("/info/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getOtherHotelInfo", summary = "getOtherHotelInfo")
    public Response getOtherHotelInfo(@PathParam("id") Long id) throws AppBaseException {
        HotelDto hotelDto = repeat(() -> hotelEndpoint.getOtherHotelInfo(id), hotelEndpoint);
        return Response.ok()
                .entity(hotelDto)
                .header("ETag", messageSigner.sign(hotelDto))
                .build();
    }

    /**
     * Generuje raport nt. działalności hotelu z zadanego okresu.
     *
     * @param from data od (dla generowanego raportu)
     * @param to   data do (dla generowanego raportu)
     * @return dane potrzebne do wygenerowania raportu
     */
    @GET
    @RolesAllowed("generateReport")
    @Path("/raport/{from}/{to}")
    @Operation(operationId = "generateReport", summary = "generateReport")
    public GenerateReportDto generateReport(@PathParam("from") Long from, @PathParam("to") Long to)
            throws AppBaseException {
        return hotelEndpoint.generateReport(from, to);
    }

    /**
     * Zwraca hotel związany z daną rezerwacją.
     *
     * @param id id rezerwacji
     * @return odpowiedź z danymi hotelu i wartością Etag.
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych.
     */
    @GET
    @RolesAllowed("getHotelForBooking")
    @Path("/hotel/booking/{id}")
    public Response getHotelForBooking(@PathParam("id") Long id) throws AppBaseException {
        HotelDto hotelDto = repeat(() -> hotelEndpoint.getHotelForBooking(id), hotelEndpoint);
        return Response.ok()
                .entity(hotelDto)
                .header("ETag", messageSigner.sign(hotelDto))
                .build();
    }
}
