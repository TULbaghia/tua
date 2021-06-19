package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.HotelException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IHotelMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.AccountFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.HotelFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.ManagerDataFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Manager odpowiadający za zarządzanie hotelami.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class HotelManager {

    @Inject
    private HotelFacade hotelFacade;

    @Inject
    private ManagerDataFacade managerDataFacade;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private HttpServletRequest servletRequest;

    @Inject
    private CityManager cityManager;

    /**
     * Zwraca hotel o podanym identyfikatorze
     *
     * @param id identyfikator hotelu
     * @return encja hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    public Hotel get(Long id) throws AppBaseException {
        return hotelFacade.find(id);
    }

    /**
     * Zwraca listę hoteli
     *
     * @return lista hoteli
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    public List<Hotel> getAll() throws AppBaseException {
        return new ArrayList<>(hotelFacade.findAll());
    }

    /**
     * Wyszukaj hotel
     *
     * @param option identyfikator hotelu
     * @return encja hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    Hotel lookForHotel(String... option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę hoteli po przefiltrowaniu
     *
     * @param fromRating dolny przedział oceny hotelu
     * @param toRating górny przedział oceny hotelu
     * @param animalTypes lista typów zwierząt
     * @return lista hoteli
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    public List<HotelDto> getAllFilter(BigDecimal fromRating,
                             BigDecimal toRating,
                             List<AnimalType> animalTypes) throws AppBaseException {
            List<Hotel> hotels = getAll()
                    .stream()
                    .filter(hotel -> {
                        try {
                            return hotel.getRating() != null
                                    && hotel.getRating().compareTo(fromRating) >= 0
                                    && hotel.getRating().compareTo(toRating) <= 0
                                    && checkHotelPetTypeListAllowed(animalTypes, hotel.getId());
                        } catch (AppBaseException e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        List<HotelDto> result = new ArrayList<>();
        for (Hotel hotel: hotels) {
            HotelDto hotelDto = Mappers.getMapper(IHotelMapper.class).toHotelDto(hotel);
            hotelDto.setCityName(hotel.getCity().getName());
            result.add(hotelDto);
        }
        return result;
    }

    /**
     * Dodaje hotel
     *
     * @param hotelDto dto z danymi miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addHotel")
    public void addHotel(NewHotelDto hotelDto) throws AppBaseException {
        Hotel hotel = new Hotel(hotelDto.getName(), hotelDto.getAddress(), hotelDto.getDescription());
        if (hotelDto.getImage() != null) {
            hotel.setImage(hotelDto.getImage());
        }

        City city = cityManager.get(hotelDto.getCityId());
        hotel.setCity(city);

        hotel.setCreatedBy(accountFacade.findByLogin(getLogin()));

        hotelFacade.create(hotel);
    }

    /**
     * Modyfikuje hotel
     *
     * @param hotel obiekt hotelu ze zmodyfikowanymi danymi.
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed({"updateOwnHotel", "updateOtherHotel"})
    public void updateHotel(Hotel hotel) throws AppBaseException {
        Account modifier = accountFacade.findByLogin(getLogin());
        hotel.setModifiedBy(modifier);
        hotelFacade.edit(hotel);
    }

    /**
     * Usuwa hotel
     *
     * @param hotelId identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteHotel")
    public void deleteHotel(Long hotelId) throws AppBaseException {
        Hotel hotel = hotelFacade.find(hotelId);
        if (hotel == null) {
            throw HotelException.notExists();
        }
        hotelFacade.remove(hotelFacade.find(hotelId));
    }

    /**
     * Przypisuje managera (po loginie) do hotelu
     *
     * @param hotelId      identyfikator hotelu
     * @param managerData rola managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addManagerToHotel")
    public void addManagerToHotel(Long hotelId, ManagerData managerData) throws AppBaseException {
        if (managerData.getHotel() != null) {
            throw HotelException.hasManager();
        }

        Hotel hotel = hotelFacade.find(hotelId);
        if (hotel == null) {
            throw HotelException.notExists();
        }

        managerData.setHotel(hotel);
        hotel.getManagerDataList().add(managerData);

        managerDataFacade.create(managerData);
        hotelFacade.edit(hotel);
    }

    /**
     * Usuwa managera (po loginie) z hotelu
     *
     * @param managerLogin login managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("deleteManagerFromHotel")
    public void deleteManagerFromHotel(String managerLogin) throws AppBaseException {
        Account managerAccount = accountFacade.findByLogin(managerLogin);
        Hotel managerHotel = managerDataFacade.findHotelByManagerId(managerLogin);
        if (managerHotel == null) {
            throw HotelException.noHotelAssigned();
        }
        ManagerData managerData = managerHotel.getManagerDataList()
                .stream()
                .filter(md -> md.getAccount().getId().equals(managerAccount.getId()))
                .findFirst()
                .get();
        managerData.setHotel(null);

        managerDataFacade.edit(managerData);
    }

    /**
     * Generuje raport nt. działalności hotelu
     *
     * @param hotelId identyfikator hotelu
     * @param from    data od
     * @param to      data do
     * @return Dane potrzebne do wygenerowania raportu
     */
    @RolesAllowed("generateReport")
    GenerateReportDto generateReport(Long hotelId, String from, String to) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca hotel o podanym identyfikatorze
     *
     * @param id identyfikator hotelu
     * @return encja hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed({"getOtherHotelInfo", "updateOtherHotel"})
    public Hotel findHotelById(Long id) throws AppBaseException {
        return hotelFacade.find(id);
    }

    /**
     * Wyszukuje Hotel przypisany do Managera o podanym id.
     *
     * @param login Managera.
     * @return wyszukiwany Hotel.
     * @throws AppBaseException gdy nie udało się pobrać danych.
     */
    @RolesAllowed({"getOwnHotelInfo", "updateOwnHotel", "generateReport"})
    public Hotel findHotelByManagerLogin(String login) throws AppBaseException {
        return managerDataFacade.findHotelByManagerId(login);
    }

    /**
     * Zwraca nazwę użytkownika pobraną z kontenera
     *
     * @return nazwa zalogowanego użytkownika
     */
    protected String getLogin() {
        return servletRequest.getUserPrincipal().getName();
    }

    /**
     * Generuje raport nt. działalności hotelu
     *
     * @param from data od (dla generowanego raportu)
     * @param to   data do (dla generowanego raportu)
     * @return lista rezerwacji z danego okresu.
     */
    @RolesAllowed("generateReport")
    public List<Booking> generateReport(Hotel hotel, Date from, Date to) throws AppBaseException {
        return hotelFacade.findAllHotelBookingsInTimeRange(hotel.getId(), from, to);
    }

    /**
     * Sprawdza czy dany hotel oferuje boxy dla danego typu zwierzęcia.
     * @param animalType rodzaj zwierzęcia
     * @param hotelId identyfikator hotelu
     * @return wartość logiczna
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    private boolean checkHotelPetTypeAllowed(AnimalType animalType, Long hotelId) throws AppBaseException {
        Optional<Box> result = hotelFacade.find(hotelId).getBoxList()
                .stream()
                .filter(box -> box.getAnimalType().equals(animalType))
                .findAny();

        return result.isPresent();
    }

    /**
     * Sprawdza czy dany hotel oferuje boxy dla danych typów zwierząt.
     * @param animalTypes lista typów zwierząt
     * @param hotelId identyfikator hotelu
     * @return wartość logiczna
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    private boolean checkHotelPetTypeListAllowed(List<AnimalType> animalTypes, Long hotelId) throws AppBaseException {
        for (AnimalType type: animalTypes){
            if (!checkHotelPetTypeAllowed(type, hotelId)){
                return false;
            }
        }
        return true;
    }

    /**
     * Zwraca hotel związany z daną rezerwacją.
     *
     * @param id identyfikator rezerwacji
     * @return hotel
     */
    @RolesAllowed("getHotelForBooking")
    public Hotel getHotelForBooking(Long id) throws AppBaseException {
        return hotelFacade.findAll().
                stream()
                .filter(hotel -> hotel.getBoxList().stream()
                        .anyMatch(b -> b.getBookingLineList().stream()
                                .anyMatch(x -> x.getBooking().getId().equals(id)))).findFirst().orElseThrow(HotelException::noHotelForBooking);
    }
}
