package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;

import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.BookingLine;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Box;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.HotelException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

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

    /**
     * Wyszukuje Hotel o podanej nazwie.
     *
     * @param name nazwa hotelu.
     * @return wyszukiwany Hotel.
     * @throws AppBaseException gdy nie udało się pobrać danych.
     */
    @PermitAll
    public Hotel findByName(String name) throws AppBaseException {
        try {
            TypedQuery<Hotel> hotelQuery = em.createNamedQuery("Hotel.findByName", Hotel.class);
            hotelQuery.setParameter("name", name);
            return hotelQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.hotelNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    @PermitAll
    public List<Hotel> findByFilter(String... filter){
        throw new UnsupportedOperationException();
    }

    @PermitAll
    @Override
    public void create(Hotel entity) throws AppBaseException {
        super.create(entity);
    }

    @PermitAll
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

    @PermitAll
    @Override
    public void remove(Hotel entity) throws AppBaseException {
        super.remove(entity);
    }

    @PermitAll
    @Override
    public Hotel find(Object id) throws AppBaseException {
        return super.find(id);
    }

    @PermitAll
    @Override
    public List<Hotel> findAll() throws AppBaseException {
        return super.findAll();
    }

    @PermitAll
    @Override
    public List<Hotel> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @PermitAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }

    /**
     * Wyszukuje rezerwacje dotyczące hotelu o podanym id, których ramy czasowe zgadzają się z podanymi.
     *
     * @param hotelId identyfikator hotelu.
     * @param from data od.
     * @param to data do.
     * @return wyszukiwany Hotel.
     * @throws AppBaseException gdy nie udało się pobrać danych.
     */
    @PermitAll
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
