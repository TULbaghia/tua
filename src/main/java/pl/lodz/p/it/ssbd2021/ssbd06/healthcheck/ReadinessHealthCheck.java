package pl.lodz.p.it.ssbd2021.ssbd06.healthcheck;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Readiness
public class ReadinessHealthCheck implements HealthCheck {

    @Inject
    private ReadinessKeeper keeper;

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("is-app-up")
                .status(keeper.isClicked())
                .build();
    }
}