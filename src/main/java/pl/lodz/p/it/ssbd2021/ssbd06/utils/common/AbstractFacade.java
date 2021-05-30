package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
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
    protected void create(T entity) throws AppBaseException {
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
    protected void edit(T entity) throws AppBaseException {
        try {
            getEntityManager().merge(entity);
            getEntityManager().flush();
        } catch (OptimisticLockException e) {
            throw AppOptimisticLockException.optimisticLockException(e);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw (ConstraintViolationException) e.getCause();
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    /**
     * Usuwa encję z bazy danych.
     *
     * @param entity obiekt encji.
     * @throws AppBaseException podczas wystąpienia błędu usuwania encji z bazy danych.
     */
    protected void remove(T entity) throws AppBaseException {
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
            getEntityManager().flush();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw (ConstraintViolationException) e.getCause();
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    protected T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Zwraca wszystkie encje
     *
     * @return lista encji
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    protected List<T> findAll() throws AppBaseException {
        try {
            CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
            cq.select(cq.from(entityClass));
            return getEntityManager().createQuery(cq).getResultList();
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    /**
     * Zwraca listę encji odpowiadających zadanym kluczom w liście
     *
     * @param range lista kluczy głównych encji
     * @return listę encji odpowiadającą kryteriom
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    protected List<T> findRange(int[] range) throws AppBaseException {
        try {
            CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
            cq.select(cq.from(entityClass));
            TypedQuery<T> q = getEntityManager().createQuery(cq);
            q.setMaxResults(range[1] - range[0] + 1);
            q.setFirstResult(range[0]);
            return q.getResultList();
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    /**
     * Zwraca ilość encji
     *
     * @return ilość encji
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    protected int count() throws AppBaseException {
        try {
            CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
            Root<T> rt = cq.from(entityClass);
            cq.select(getEntityManager().getCriteriaBuilder().count(rt));
            TypedQuery<Long> q = getEntityManager().createQuery(cq);
            return q.getSingleResult().intValue();
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }
}
