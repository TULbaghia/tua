package pl.lodz.p.it.ssbd2021.ssbd06.security;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Filtr sprawdzający nagłówek odpowiedzialny za kontrolę wersji
 */
@Provider
@SignatureValidatorFilterBinding
public class SignatureValidatorFilter implements ContainerRequestFilter {

    @Inject
    private MessageVerifier messageVerifier;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String header = requestContext.getHeaderString("If-Match");
        if (header == null || header.isEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
        } else if (!messageVerifier.validateSignature(header)) {
            requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
        }
    }
}
