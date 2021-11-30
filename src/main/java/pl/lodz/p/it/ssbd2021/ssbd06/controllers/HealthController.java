package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@ApplicationScoped
public class HealthController {

    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    @Getter
    @Setter
    private boolean isClicked;

    @Produces
    @Liveness
    public HealthCheck livenessCheck() {
        return () -> HealthCheckResponse.named("heap-memory")
                .status(getUsedMemory() < 0.9 * getMaxMemory())
                .build();
    }

    @Produces
    @Readiness
    public HealthCheck readinessCheck() {
        return () -> HealthCheckResponse.named("is-app-up")
                .status(isClicked())
                .build();
    }

    @PUT
    @Path("/health/toggle")
    public void toggle() {
        setClicked(!isClicked());
    }

    private long getUsedMemory() {
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }

    private long getMaxMemory() {
        return memoryMXBean.getHeapMemoryUsage().getMax();
    }
}