package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class AbstractFacade<T extends AbstractEntity> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    /**
     * Utrwala encję w bazie danych.
     *
     * @param entity obiekt encji
     * @throws AppBaseException podczas wystąpienia błędu utrwalania w bazie danych
     */
    public void create(T entity) throws AppBaseException {
        try {
            getEntityManager().persist(entity);
            getEntityManager().flush();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw (ConstraintViolationException) e.getCause();
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    /**
     * Aktualizuje encję w bazie danych.
     *
     * @param entity obiekt encji.
     * @throws AppBaseException gdy wystąpił błąd blokady optymistycznej lub błąd związany z bazą danych.
     */
    public void edit(T entity) throws AppBaseException {
        try {
            getEntityManager().merge(entity);
            getEntityManager().flush();
        } catch (OptimisticLockException e) {
            throw AppOptimisticLockException.optimisticLockException(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        TypedQuery<T> q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        TypedQuery<Long> q = getEntityManager().createQuery(cq);
        return q.getSingleResult().intValue();
    }
}
