package pl.lodz.p.it.ssbd2021.ssbd06.mok.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Role;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.*;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.util.Date;
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
            } else if (e.getCause().getMessage().contains(Role.ROLE_ACCESS_LEVEL_ACCOUNT_ID_CONSTRAINT)) {
                throw RoleException.alreadyGranted();
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    /**
     * Wyszukuje konto na podstawie loginu
     *
     * @param login login użytkownika
     * @return obiekt encji konta o podanym loginie
     * @throws AppBaseException gdy konto nie zostało znalezione, lub wystąpił problem z bazą danych.
     */
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
     *
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
     *
     * @return lista kont
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych
     */
    @Override
    @PermitAll
    public List<Account> findAll() throws AppBaseException {
        return super.findAll();
    }

    /**
     * Przeprowadza operację edycji obiektu encji.
     * @param entity obiekt encji.
     * @throws AppBaseException gdy nie udało się przeprowadzić operacji edycji
     */
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
            } else  if (e.getCause().getMessage().contains(Role.ROLE_ACCESS_LEVEL_ACCOUNT_ID_CONSTRAINT)) {
                throw RoleException.alreadyGranted();
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    /**
     * Zwraca listę kont niezweryfikowanych
     * @param expirationDate data wygaśnięcia możliwości aktywacji konta
     * @return lista kont
     * @throws AppBaseException gdy nie udało się przeprowadzić operacji wyświetlenia listy kont
     */
    @PermitAll
    public List<Account> findUnverifiedBefore(Date expirationDate) throws AppBaseException {
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

    @PermitAll
    @Override
    public void remove(Account entity) throws AppBaseException {
        super.remove(entity);
    }

    @DenyAll
    @Override
    public Account find(Object id) {
        return super.find(id);
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
