package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
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
public class AccountFacadeMoh extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacadeMoh() {
        super(Account.class);
    }

    /**
     * Wyszukuje konto na podstawie loginu
     *
     * @param login login użytkownika
     * @return obiekt encji konta o podanym loginie
     * @throws AppBaseException gdy konto nie zostało znalezione, lub wystąpił problem z bazą danych.
     */
    @RolesAllowed({"Client", "Manager", "Admin"})
    public Account findByLogin(String login) throws AppBaseException {
        try {
            TypedQuery<Account> accountTypedQuery = em.createNamedQuery("Account.findByLogin", Account.class);
            accountTypedQuery.setParameter("login", login);
            return accountTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    @DenyAll
    @Override
    public void create(Account entity) throws AppBaseException {
        super.create(entity);
    }

    @DenyAll
    @Override
    public void edit(Account entity) throws AppBaseException {
        super.edit(entity);
    }

    @DenyAll
    @Override
    public void remove(Account entity) throws AppBaseException {
        super.remove(entity);
    }

    @DenyAll
    @Override
    public Account find(Object id) throws AppBaseException {
        return super.find(id);
    }

    @DenyAll
    @Override
    public List<Account> findAll() throws AppBaseException {
        return super.findAll();
    }

    @DenyAll
    @Override
    public List<Account> findRange(int[] range) throws AppBaseException {
        return super.findRange(range);
    }

    @DenyAll
    @Override
    public int count() throws AppBaseException {
        return super.count();
    }
}
