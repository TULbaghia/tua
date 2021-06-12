package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.HotelException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.RoleException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
     * @return lista hoteli
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @PermitAll
    List<Hotel> getAllFilter(String... option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje hotel
     *
     * @param hotelDto dto z danymi miasta
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addHotel")
    void addHotel(NewHotelDto hotelDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Modyfikuje hotel
     *
     * @param hotel obiekt hotelu ze zmodyfikowanymi danymi.
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed({"updateOwnHotel", "updateOtherHotel"})
    public void updateHotel(Hotel hotel) throws AppBaseException {
        Account modifier = accountFacade.findByLogin(servletRequest.getUserPrincipal().getName());
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
     * @param managerLogin login managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addManagerToHotel")
    public void addManagerToHotel(Long hotelId, String managerLogin) throws AppBaseException {
        Account account = accountFacade.findByLogin(managerLogin);
        Set<Role> roleList = account.getRoleList();
        Role managerRole = null;
        for (Role role : roleList) {
            if (role.getAccessLevel() == AccessLevel.MANAGER && role.isEnabled()) {
                managerRole = role;
            }
        }
        if (managerRole == null) {
            throw RoleException.accountNotManager();
        }
        ManagerData managerData = managerDataFacade.find(managerRole.getId());
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
    void deleteManagerFromHotel(String managerLogin) throws AppBaseException {
        throw new UnsupportedOperationException();
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
}
