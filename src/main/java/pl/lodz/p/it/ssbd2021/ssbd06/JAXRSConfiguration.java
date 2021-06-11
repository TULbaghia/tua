package pl.lodz.p.it.ssbd2021.ssbd06;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Konfiguruje JAX-RS dla aplikacji.
 */
@ApplicationPath("resources")
@OpenAPIDefinition(info = @Info(
        title = "ssbd06 application",
        version = "1.0.0"
))
public class JAXRSConfiguration extends Application {

}
