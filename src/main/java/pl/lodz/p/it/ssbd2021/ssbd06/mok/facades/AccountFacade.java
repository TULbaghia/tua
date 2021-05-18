package pl.lodz.p.it.ssbd2021.ssbd06.mok.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AccountException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.util.List;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends AbstractFacade<Account> {

    @PersistenceContext(unitName = "ssbd06mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }

    /**
     * Utrwala encję w bazie danych oraz sprawdza warunki poprawności.
     *
     * @param entity obiekt encji konta
     * @throws AppBaseException podczas wystąpienia błędu utrwalania w bazie danych
     */
    @Override
    @PermitAll
    public void create(Account entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(Account.LOGIN_CONSTRAINT)) {
                throw AccountException.loginExists(e.getCause());
            } else if (e.getCause().getMessage().contains(Account.CONTACT_NUMBER_CONSTRAINT)) {
                throw AccountException.contactNumberException(e.getCause());
            } else if (e.getCause().getMessage().contains(Account.EMAIL_CONSTRAINT)) {
                throw AccountException.emailExists(e.getCause());
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @PermitAll
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

    /**
     * Zwraca konto o podanym adresie email.
     * @return obiekt Account.
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych.
     */
    @PermitAll
    public Account findByEmail(String email) throws AppBaseException {
        try {
            TypedQuery<Account> accountTypedQuery = em.createNamedQuery("Account.findByEmail", Account.class);
            accountTypedQuery.setParameter("email", email);
            return accountTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    /**
     * Zwraca listę wszystkich kont w systemie.
     * @return lista kont
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @Override
    @PermitAll
    public List<Account> findAll() throws AppBaseException{
            return super.findAll();
    }

    @Override
    @PermitAll
    public void edit(Account entity) throws AppBaseException {
        try {
            super.edit(entity);
        } catch (ConstraintViolationException e) {
            if (e.getCause().getMessage().contains(Account.LOGIN_CONSTRAINT)) {
                throw AccountException.loginExists(e.getCause());
            } else if (e.getCause().getMessage().contains(Account.CONTACT_NUMBER_CONSTRAINT)) {
                throw AccountException.contactNumberException(e.getCause());
            } else if (e.getCause().getMessage().contains(Account.EMAIL_CONSTRAINT)) {
                throw AccountException.emailExists(e.getCause());
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @PermitAll
    public List<Account> findUnverifiedBefore(long expirationDate) throws AppBaseException {
        try {
            TypedQuery<Account> accountTypedQuery = em.createNamedQuery("Account.findUnverified", Account.class);
            accountTypedQuery.setParameter("date", expirationDate);
            return accountTypedQuery.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.accountNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }
}
