package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.managers.ScheduledTasksManager;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.annotation.security.RunAs;
import javax.ejb.*;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 * Odpowiada za czasowe uruchamianie zadań.
 */
@Startup
@Singleton
@Interceptors({LoggingInterceptor.class})
@Log
@RunAs("System")
public class ScheduledTaskRunner {

    @Inject
    private ScheduledTasksManager scheduledTasksManager;

    /**
     * Czasowo wykonuje akcję ponawiania wysyłania wiadomości / usunięcia użytkowników niepotwierdzonych
     *
     * @param time
     * @throws AppBaseException w przypadku gdy operacja zakończy się niepowodzeniem
     */
    @Schedule(hour = "*", minute = "0", second = "0", info = "Wykonuje metodę co godzinę począwszy od pełnej godziny", persistent = false)
    private void deleteUnverifiedAccounts(Timer time) throws AppBaseException {
        scheduledTasksManager.deleteUnverifiedAccounts(time);
    }

    /**
     * Czasowo wykonuje akcję ponawiania wiadomości oraz anulowania procesu zmiany adresu email
     *
     * @param time
     * @throws AppBaseException w przypadku gdy operacja zakończy się niepowodzeniem
     */
    @Schedule(hour = "*", minute = "0", second = "0", info = "Wykonuje metodę co godzinę począwszy od pełnej godziny", persistent = false)
    private void sendRepeatedEmailChange(Timer time) throws AppBaseException {
        scheduledTasksManager.sendRepeatedEmailChange(time);
    }
}
