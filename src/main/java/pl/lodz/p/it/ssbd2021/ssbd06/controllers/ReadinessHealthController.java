package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@ApplicationScoped
public class ReadinessHealthController implements HealthCheck {

    @Getter
    @Setter
    private boolean isClicked;

    @GET
    @Path("/health/toggle")
    public String toggle() {
        setClicked(!isClicked());
        return String.valueOf(isClicked());
    }

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("is-app-up")
                .status(isClicked())
                .build();
    }
}