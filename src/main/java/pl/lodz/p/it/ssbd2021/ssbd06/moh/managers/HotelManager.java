package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.HotelException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.RoleException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.GenerateReportDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewHotelDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.UpdateHotelDto;
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
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
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

    @Inject
    private CityManager cityManager;

    /**
     * Zwraca hotel o podanym identyfikatorze
     *
     * @param id identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return encja hotelu
     */
    @PermitAll
    public Hotel get(Long id) throws AppBaseException {
        return hotelFacade.find(id);
    }

    /**
     * Zwraca listę hoteli
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista hoteli
     */
    @PermitAll
    public List<Hotel> getAll() throws AppBaseException {
        return new ArrayList<>(hotelFacade.findAll());
    }

    /**
     * Wyszukaj hotel
     *
     * @param option identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return encja hotelu
     */
    @PermitAll
    Hotel lookForHotel(String ...option) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę hoteli po przefiltrowaniu
     *
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista hoteli
     */
    @PermitAll
    List<Hotel> getAllFilter(String ...option) throws AppBaseException {
        throw new UnsupportedOperationException();
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
     * @param hotelId identyfikator hotelu
     * @param managerLogin login managera którego przypisać do hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    @RolesAllowed("addManagerToHotel")
    public void addManagerToHotel(Long hotelId, String managerLogin) throws AppBaseException {
        Account account = accountFacade.findByLogin(managerLogin);
        Set<Role> roleList = account.getRoleList();
        Role managerRole = null;
        for (Role role: roleList) {
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
     * @param from data od
     * @param to data do
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
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return encja hotelu
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
    @RolesAllowed({"getOwnHotelInfo", "updateOwnHotel"})
    public Hotel findHotelByManagerLogin(String login) throws AppBaseException {
        return managerDataFacade.findHotelByManagerId(login);
    }

    protected String getLogin() {
        return servletRequest.getUserPrincipal().getName();
    }
}
