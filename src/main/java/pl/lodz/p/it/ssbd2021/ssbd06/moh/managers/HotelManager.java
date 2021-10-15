package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AnimalType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.HotelException;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IHotelMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.HotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.facades.*;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
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
    private BoxFacade boxFacade;

    @Inject
    private ManagerDataFacadeMoh managerDataFacadeMoh;

    @Inject
    private AccountFacadeMoh accountFacadeMoh;

    @Inject
    private BookingFacade bookingFacade;

    @Inject
    private HttpServletRequest servletRequest;

    @Inject
    private CityManager cityManager;

    @Resource(name = "sessionContext")
    SessionContext sessionContext;

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
     * Zwraca listę hoteli po przefiltrowaniu
     *
     * @param fromRating  dolny przedział oceny hotelu
     * @param toRating    górny przedział oceny hotelu
     * @param animalTypes lista typów zwierząt
     * @return lista hoteli
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    public List<HotelDto> getAllFilter(BigDecimal fromRating,
                                       BigDecimal toRating,
                                       List<AnimalType> animalTypes,
                                       String searchQuery) throws AppBaseException {
        List<Hotel> hotels = getAll()
                .stream()
                .filter(hotel -> {
                    try {
                        return Objects.requireNonNullElse(hotel.getRating(), BigDecimal.ZERO).compareTo(fromRating) >= 0
                                &&
                                Objects.requireNonNullElse(hotel.getRating(), BigDecimal.TEN).compareTo(toRating) <= 0
                                && checkHotelPetTypeListAllowed(animalTypes, hotel.getId())
                                && checkHotelNameContainsString(hotel, searchQuery);
                    } catch (AppBaseException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
        List<HotelDto> result = new ArrayList<>();
        for (Hotel hotel : hotels) {
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

        hotel.setCreatedBy(accountFacadeMoh.findByLogin(getLogin()));

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
        Account modifier = accountFacadeMoh.findByLogin(getLogin());
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

        List<Booking> activeBookings = bookingFacade.findAllActive();
        for (Booking booking : activeBookings) {
            List<BookingLine> bookingLines = booking.getBookingLineList();
            for (BookingLine bookingLine : bookingLines) {
                if (bookingLine.getBox().getHotel() == hotel) {
                    throw HotelException.activeBooking();
                }
            }
        }

        List<Box> boxList = boxFacade.findAll();
        for (Box box : boxList) {
            if (box.getHotel() != null && box.getHotel().getId().equals(hotelId)) {
                box.setHotel(null);
                box.setDelete(true);
                boxFacade.edit(box);
            }
        }

        List<City> citiesList = cityManager.getAll();
        for (City city : citiesList) {
            city.getHotelList().remove(hotel);
        }

        hotelFacade.remove(hotelFacade.find(hotelId));
    }

    /**
     * Przypisuje managera (po loginie) do hotelu
     *
     * @param hotelId     identyfikator hotelu
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
        managerData.setModifiedBy(accountFacadeMoh.findByLogin(sessionContext.getCallerPrincipal().getName()));
        hotel.getManagerDataList().add(managerData);
        managerDataFacadeMoh.edit(managerData);
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
        Account managerAccount = accountFacadeMoh.findByLogin(managerLogin);
        Hotel managerHotel = managerDataFacadeMoh.findHotelByManagerId(managerLogin);
        if (managerHotel == null) {
            throw HotelException.noHotelAssigned();
        }
        ManagerData managerData = managerHotel.getManagerDataList()
                .stream()
                .filter(md -> md.getAccount().getId().equals(managerAccount.getId()))
                .findFirst()
                .get();
        managerData.setHotel(null);
        managerData.setModifiedBy(accountFacadeMoh.findByLogin(sessionContext.getCallerPrincipal().getName()));
        managerDataFacadeMoh.edit(managerData);
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
    @RolesAllowed({"getOwnHotelInfo", "updateOwnHotel", "generateReport", "deleteManagerFromHotel"})
    public Hotel findHotelByManagerLogin(String login) throws AppBaseException {
        return managerDataFacadeMoh.findHotelByManagerId(login);
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
     *
     * @param animalType rodzaj zwierzęcia
     * @param hotelId    identyfikator hotelu
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
     *
     * @param animalTypes lista typów zwierząt
     * @param hotelId     identyfikator hotelu
     * @return wartość logiczna
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    private boolean checkHotelPetTypeListAllowed(List<AnimalType> animalTypes, Long hotelId) throws AppBaseException {
        for (AnimalType type : animalTypes) {
            if (!checkHotelPetTypeAllowed(type, hotelId)) {
                return false;
            }
        }
        return true;
    }

    @PermitAll
    public boolean checkHotelNameContainsString(Hotel hotel, String text) {
        return hotel.getName().toLowerCase().contains(text.toLowerCase())
                || hotel.getAddress().toLowerCase().contains(text.toLowerCase())
                || hotel.getCity().getName().toLowerCase().contains(text.toLowerCase());
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
                                .anyMatch(x -> x.getBooking().getId().equals(id)))).findFirst()
                .orElseThrow(HotelException::noHotelForBooking);
    }
}
