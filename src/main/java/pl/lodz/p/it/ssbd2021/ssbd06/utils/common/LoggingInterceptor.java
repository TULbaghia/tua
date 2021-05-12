package pl.lodz.p.it.ssbd2021.ssbd06.utils.common;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interceptor zapewniający rejestrowanie szczegółów wywowłania metod komponentów EJB w dzienniku zdarzeń.
 */
public class LoggingInterceptor {
    private static final Logger LOGGER = Logger.getLogger(LoggingInterceptor.class.getName());

    @Resource
    private SessionContext sessionContext;

    /**
     * Metoda logująca szczegóły wywołania metod do dziennika zdarzeń.
     *
     * @param invocationContext informację o opakowanej metodzie
     * @return wartość zwracana przez opakowywaną metodę
     * @throws Exception wyjątek rzucany przez opakowaną metodę
     */
    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Exception {
        String className = invocationContext.getTarget().getClass().getName();
        String methodName = invocationContext.getMethod().getName();
        String caller = sessionContext.getCallerPrincipal() != null ? sessionContext.getCallerPrincipal().getName() : "Guest";

        StringBuilder params = new StringBuilder();
        if(invocationContext.getParameters() != null) {
            for(Object o : invocationContext.getParameters()) {
                params.append(o.getClass().getSimpleName()).append(", ");
            }
            params.delete(params.length() - 2, params.length());
        }

        LOGGER.log(Level.INFO, "{0}.{1}({2}) is called by {3}", new Object[]{className, methodName, params.toString(), caller});

        Object result;

        try {
            result = invocationContext.proceed();
        } catch (Exception exception) {
            String cause = exception.getCause() != null ? ", caused by " + exception.getCause().getClass().getName() : "";

            LOGGER.log(Level.WARNING, "{0}.{1}({2}) threw {3}{4}", new Object[]{className, methodName, params.toString(), exception.getClass().getName(), cause});
            throw exception;
        }

        LOGGER.log(Level.INFO, "{0}.{1}({2}) returned {3}", new Object[]{className, methodName, params.toString(), result});

        return result;
    }
}
