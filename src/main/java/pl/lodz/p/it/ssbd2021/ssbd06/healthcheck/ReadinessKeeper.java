package pl.lodz.p.it.ssbd2021.ssbd06.healthcheck;

import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReadinessKeeper {

    @Getter
    @Setter
    private boolean isClicked = true;
}