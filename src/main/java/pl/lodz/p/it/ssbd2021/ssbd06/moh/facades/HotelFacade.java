package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
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
import java.util.Date;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class HotelFacade extends AbstractFacade<Hotel> {

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HotelFacade() {
        super(Hotel.class);
    }

    @RolesAllowed("addHotel")
    @Override
    public void create(Hotel entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(Hotel.HOTEL_NAME_CONSTRAINT)) {
                throw HotelException.hotelNameExists(e.getCause());
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @RolesAllowed({"updateOwnHotel", "updateOtherHotel", "addManagerToHotel", "addHotelRating"})
    @Override
    public void edit(Hotel entity) throws AppBaseException {
        try {
            super.edit(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(Hotel.HOTEL_NAME_CONSTRAINT)) {
                throw HotelException.hotelNameExists(e.getCause());
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @RolesAllowed("deleteHotel")
    @Override
    public void remove(Hotel entity) throws AppBaseException {
        try {
            super.remove(entity);
        } catch (ConstraintViolationException e) {
            if (entity.getManagerDataList() != null) {
                throw HotelException.deleteHasManager();
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @PermitAll
    @Override
    public Hotel find(Object id) throws AppBaseException {
        try {
            TypedQuery<Hotel> hotelTypedQuery = em.createNamedQuery("Hotel.findById", Hotel.class);
            hotelTypedQuery.setParameter("id", id);
            return hotelTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.hotelNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    @PermitAll
    @Override
    public List<Hotel> findAll() throws AppBaseException {
        return super.findAll();
    }

    @DenyAll
    @Override
    public List<Hotel> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @DenyAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }

    /**
     * Wyszukuje rezerwacje dotyczące hotelu o podanym id, których ramy czasowe zgadzają się z podanymi.
     *
     * @param hotelId identyfikator hotelu.
     * @param from    data od.
     * @param to      data do.
     * @return wyszukiwany Hotel.
     * @throws AppBaseException gdy nie udało się pobrać danych.
     */
    @RolesAllowed("generateReport")
    public List<Booking> findAllHotelBookingsInTimeRange(Long hotelId, Date from, Date to) throws AppBaseException {
        try {
            TypedQuery<Booking> hotelQuery = em.createNamedQuery("BookingLine.findAllByHotelId", Booking.class);
            hotelQuery.setParameter("hotelId", hotelId);
            hotelQuery.setParameter("from", from);
            hotelQuery.setParameter("to", to);
            return hotelQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.hotelNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }
}
