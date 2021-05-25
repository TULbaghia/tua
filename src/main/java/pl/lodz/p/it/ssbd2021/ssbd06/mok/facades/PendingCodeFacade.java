package pl.lodz.p.it.ssbd2021.ssbd06.mok.facades;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.enums.CodeType;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.CodeException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import java.util.Date;
import java.util.List;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PendingCodeFacade extends AbstractFacade<PendingCode> {

    @PersistenceContext(unitName = "ssbd06mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PendingCodeFacade() {
        super(PendingCode.class);
    }

    @PermitAll
    public PendingCode findByCode(String code) throws AppBaseException {
        try {
            TypedQuery<PendingCode> query = em.createNamedQuery("PendingCode.findByCode", PendingCode.class);
            query.setParameter("code", code);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.pendingCodeNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    @PermitAll
    public List<PendingCode> findResetCodesByAccount(Account account) throws AppBaseException {
        try {
            TypedQuery<PendingCode> query = em.createNamedQuery("PendingCode.findResetCodesByAccount", PendingCode.class);
            query.setParameter("account", account);
            return query.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.pendingCodeNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    @Override
    @PermitAll
    public void create(PendingCode entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if(e.getCause().getMessage().contains(PendingCode.PENDING_CODE_CONSTRAINT)){
                throw CodeException.codeDuplicated(e.getCause());
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @Override
    @PermitAll
    public void edit(PendingCode entity) throws AppBaseException {
        try {
            super.edit(entity);
        } catch (ConstraintViolationException e) {
            if(e.getCause().getMessage().contains(PendingCode.PENDING_CODE_CONSTRAINT)){
                throw CodeException.codeDuplicated(e.getCause());
            }
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @PermitAll
    public PendingCode findNotUsedByAccount(Account account) throws AppBaseException {
        try {
            TypedQuery<PendingCode> query = em.createNamedQuery("PendingCode.findNotUsedByAccount", PendingCode.class);
            query.setParameter("account", account);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.pendingCodeNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    /**
     * Zwraca listę kont użytkowników, które posiadają przypisane kody o danym typie i statusie.
     * @param codeType typ kodów, które mają zostać zwrócone.
     * @param expirationDate parametr precyzujący czas ponownego przesłania wiadomości ze zmianą adresu email.
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych.
     */
    @PermitAll
    public List<Account> findAllAccountsWithUnusedCodes(CodeType codeType, Date expirationDate) throws AppBaseException {
        try {
            TypedQuery<Account> query = em.createNamedQuery("PendingCode.findAllAccountsWithUnusedCodes", Account.class);
            query.setParameter("codeType", codeType);
            query.setParameter("date", expirationDate);
            return query.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.pendingCodeNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    /**
     * Zwraca kod, który nie został wykorzystany, dla wskazanego konta użytkownika.
     * @param account konto użytkownika, dla którego wyszukiwany jest kod.
     * @throws AppBaseException podczas wystąpienia problemu z bazą danych.
     */
    @PermitAll
    public PendingCode findUnusedCodeByAccount(Account account, CodeType codeType) throws AppBaseException {
        try {
            TypedQuery<PendingCode> query = em.createNamedQuery("PendingCode.findUnusedCodeByAccount", PendingCode.class);
            query.setParameter("account", account);
            query.setParameter("codeType", codeType);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw NotFoundException.pendingCodeNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }

    public List<PendingCode> findAllUnusedByCodeTypeAndBeforeAndAttemptCount(CodeType codeType, Date dateBefore, int attempts) throws AppBaseException {
        try {
            TypedQuery<PendingCode> query = em.createNamedQuery("PendingCode.findAllUnusedByCodeTypeAndBeforeAndAttemptCount", PendingCode.class);
            query.setParameter("type", codeType);
            query.setParameter("date", dateBefore);
            query.setParameter("attempts", attempts);
            return query.getResultList();
        } catch (NoResultException e) {
            throw NotFoundException.pendingCodeNotFound(e);
        } catch (PersistenceException e) {
            throw DatabaseQueryException.databaseQueryException(e);
        }
    }
}
