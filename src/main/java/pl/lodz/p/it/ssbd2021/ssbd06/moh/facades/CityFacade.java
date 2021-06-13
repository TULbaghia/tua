package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.City;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.*;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({LoggingInterceptor.class})
public class CityFacade extends AbstractFacade<City> {

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CityFacade() {
        super(City.class);
    }

    @PermitAll
    @Override
    public void create(City entity) throws AppBaseException {
        super.create(entity);
    }

    @PermitAll
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

    @PermitAll
    @Override
    public void remove(City entity) throws AppBaseException {
        super.remove(entity);
    }

    @PermitAll
    @Override
    public City find(Object id) throws AppBaseException {
        return super.find(id);
    }

    @PermitAll
    @Override
    public List<City> findAll() throws AppBaseException {
        return super.findAll();
    }

    @PermitAll
    @Override
    public List<City> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @PermitAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }

    /**
     * Wyszukuje obiekt City o podanej nazwie.
     *
     * @param name nazwa miasta.
     * @return wyszukiwane miasto.
     * @throws AppBaseException gdy nie udało się pobrać danych
     */
    @PermitAll
    public City findByName(String name) throws AppBaseException {
        try {
            TypedQuery<City> cityQuery = em.createNamedQuery("City.findByName", City.class);
            cityQuery.setParameter("name", name);
            return cityQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.cityNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }
}
