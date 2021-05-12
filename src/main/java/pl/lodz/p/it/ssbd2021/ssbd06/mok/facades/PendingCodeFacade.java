package pl.lodz.p.it.ssbd2021.ssbd06.mok.facades;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.PendingCode;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.DatabaseQueryException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.NotFoundException;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import java.util.List;

@Stateless
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
    public void create(PendingCode entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            throw DatabaseQueryException.databaseQueryException(e.getCause());
        }
    }

    @Override
    @PermitAll
    public void edit(PendingCode entity) throws AppBaseException {
        super.edit(entity);
    }
}
