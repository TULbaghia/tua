package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.City;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.CityException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class CityFacade extends AbstractFacade<City> {

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    public CityFacade() {
        super(City.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @RolesAllowed("addCity")
    @Override
    public void create(City entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(City.CITY_CONSTRAINT)) {
                throw CityException.cityNameExists(e.getCause());
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @RolesAllowed("updateCity")
    @Override
    public void edit(City entity) throws AppBaseException {
        try {
            super.edit(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(City.CITY_CONSTRAINT)) {
                throw CityException.cityNameExists(e.getCause());
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @RolesAllowed("deleteCity")
    @Override
    public void remove(City entity) throws AppBaseException {
        super.remove(entity);
    }

    @RolesAllowed({"addHotel", "deleteCity", "updateCity", "getCity", "updateOwnHotel", "updateOtherHotel"})
    @Override
    public City find(Object id) throws AppBaseException {
        return super.find(id);
    }

    @RolesAllowed("getAllCities")
    @Override
    public List<City> findAll() throws AppBaseException {
        return super.findAll();
    }

    @DenyAll
    @Override
    public List<City> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @DenyAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }
}
