package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@ApplicationScoped
public class LivenessHealthController implements HealthCheck {

    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("heap-memory")
                .status(getUsedMemory() < 0.9 * getMaxMemory())
                .build();
    }

    private long getUsedMemory() {
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }

    private long getMaxMemory() {
        return memoryMXBean.getHeapMemoryUsage().getMax();
    }
}