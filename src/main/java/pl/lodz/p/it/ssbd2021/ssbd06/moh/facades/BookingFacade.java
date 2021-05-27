package pl.lodz.p.it.ssbd2021.ssbd06.moh.facades;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.NotImplementedException;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Booking;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractFacade;

import java.util.List;

@Stateless
public class BookingFacade extends AbstractFacade<Booking>{

    @PersistenceContext(unitName = "ssbd06mohPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BookingFacade() {
        super(Booking.class);
    }

    @PermitAll
    public List<Booking> findAllActive(){
        throw new NotImplementedException();
    }

    @PermitAll
    public List<Booking> findAllArchived(){
        throw new NotImplementedException();
    }
}
