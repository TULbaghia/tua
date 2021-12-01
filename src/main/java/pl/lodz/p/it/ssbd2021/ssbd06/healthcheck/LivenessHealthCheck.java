package pl.lodz.p.it.ssbd2021.ssbd06.healthcheck;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@ApplicationScoped
@Liveness
public class LivenessHealthCheck implements HealthCheck {

//    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    @Inject
    private ReadinessKeeper keeper;

//    @Override
//    public HealthCheckResponse call() {
//        return HealthCheckResponse.named("heap-memory")
//                .status(getUsedMemory() < 0.9 * getMaxMemory())
//                .build();
//    }

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("is-app-up")
                .status(keeper.isClicked())
                .build();
    }

//    private long getUsedMemory() {
//        return memoryMXBean.getHeapMemoryUsage().getUsed();
//    }
//
//    private long getMaxMemory() {
//        return memoryMXBean.getHeapMemoryUsage().getMax();
//    }
}