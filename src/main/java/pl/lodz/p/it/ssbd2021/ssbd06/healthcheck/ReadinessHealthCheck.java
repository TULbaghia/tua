package pl.lodz.p.it.ssbd2021.ssbd06.healthcheck;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@ApplicationScoped
@Readiness
public class ReadinessHealthCheck implements HealthCheck {

//    @Inject
//    private ReadinessKeeper keeper;

    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    private long getUsedMemory() {
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }

    private long getMaxMemory() {
        return memoryMXBean.getHeapMemoryUsage().getMax();
    }

//    @Override
//    public HealthCheckResponse call() {
//        return HealthCheckResponse.named("is-app-up")
//                .status(keeper.isClicked())
//                .build();
//    }

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("heap-memory")
                .status(getUsedMemory() < 0.9 * getMaxMemory())
                .build();
    }
}