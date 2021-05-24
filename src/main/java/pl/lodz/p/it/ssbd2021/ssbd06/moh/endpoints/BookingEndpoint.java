package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBookingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.BookingEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Endpoint odpowiadający za zarządzanie rezerwacjami.
 */
@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BookingEndpoint extends AbstractEndpoint implements BookingEndpointLocal {

    @Override
    public BookingDto get(Long id) throws AppBaseException {
        return null;
    }

    @Override
    public List<BookingDto> getAll() throws AppBaseException {
        return null;
    }

    @Override
    public List<BookingDto> getAll(String... option) throws AppBaseException {
        return null;
    }

    @Override
    public void addBooking(NewBookingDto bookingDto) throws AppBaseException {

    }

    @Override
    public void cancelBooking(Long bookingId) throws AppBaseException {

    }

    @Override
    public void endBooking(Long bookingId) throws AppBaseException {

    }

    @Override
    public void startBooking(Long bookingId) throws AppBaseException {

    }

    @Override
    public List<BookingDto> showActiveBooking() throws AppBaseException {
        return null;
    }

    @Override
    public List<BookingDto> showEndedBooking() throws AppBaseException {
        return null;
    }
}
