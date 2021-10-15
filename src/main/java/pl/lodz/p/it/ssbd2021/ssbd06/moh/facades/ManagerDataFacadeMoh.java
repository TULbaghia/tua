package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.ManagerData;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.HotelException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.util.List;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ManagerDataFacadeMoh extends AbstractFacade<ManagerData> {

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @PermitAll
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ManagerDataFacadeMoh() {
        super(ManagerData.class);
    }

    @DenyAll
    @Override
    public void create(ManagerData entity) throws AppBaseException {
        super.create(entity);
    }

    @RolesAllowed({"addManagerToHotel", "deleteManagerFromHotel"})
    @Override
    public void edit(ManagerData entity) throws AppBaseException {
        super.edit(entity);
    }

    @DenyAll
    @Override
    public void remove(ManagerData entity) throws AppBaseException {
        super.remove(entity);
    }

    @RolesAllowed("addManagerToHotel")
    @Override
    public ManagerData find(Object id) throws AppBaseException {
        return super.find(id);
    }

    @DenyAll
    @Override
    public List<ManagerData> findAll() throws AppBaseException {
        return super.findAll();
    }

    /**
     * Wyszukuje Hotel przypisany do Managera o podanym id.
     *
     * @param login login Managera.
     * @return wyszukiwany Hotel.
     * @throws AppBaseException gdy nie udało się pobrać danych.
     */
    @RolesAllowed({"getOwnHotelInfo", "updateOwnHotel", "generateReport", "deleteManagerFromHotel"})
    public Hotel findHotelByManagerId(String login) throws AppBaseException {
        try {
            TypedQuery<Hotel> managerHotelQuery =
                    em.createNamedQuery("ManagerData.findHotelByManagerLogin", Hotel.class);
            managerHotelQuery.setParameter("login", login);
            return managerHotelQuery.getSingleResult();
        } catch (NoResultException e) {
            throw HotelException.noHotelAssigned();
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    @DenyAll
    @Override
    public List<ManagerData> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @DenyAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }
}