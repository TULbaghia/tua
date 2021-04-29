package pl.lodz.p.it.ssbd2021.ssbd06.auth.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AuthValidationException;
import pl.lodz.p.it.ssbd2021.ssbd06.security.JWTGenerator;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;

@Stateful
public class AuthEndpoint implements AuthEndpointLocal {

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Override
    public String login(String login, String password) throws AppBaseException {
        Credential credential = new UsernamePasswordCredential(login, new Password(password));
        CredentialValidationResult result = identityStoreHandler.validate(credential);
        if(result.getStatus() == CredentialValidationResult.Status.VALID) {
            return JWTGenerator.generateJWTString(result);
        } else {
            throw new AuthValidationException("Invalid credential");
        }
    }
}
