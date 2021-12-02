package pl.lodz.p.it.ssbd2021.ssbd06.healthcheck;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@ApplicationScoped
@Path("/health")
public class LivenessChangeController {

    @Inject
    private LivenessKeeper keeper;

    @GET
    @Path("toggle")
    public String toggle() {
        keeper.setClicked(!keeper.isClicked());
        return String.valueOf(keeper.isClicked());
    }
}