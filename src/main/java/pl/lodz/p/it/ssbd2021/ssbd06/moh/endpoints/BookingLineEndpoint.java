package pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.moh.endpoints.interfaces.BookingLineEndpointLocal;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

/**
 * Endpoint odpowiadający za zarządzanie klatkami w rezerwacji.
 */
@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class BookingLineEndpoint extends AbstractEndpoint implements BookingLineEndpointLocal {
}
