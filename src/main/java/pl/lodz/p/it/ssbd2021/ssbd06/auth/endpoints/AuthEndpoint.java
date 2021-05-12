package pl.lodz.p.it.ssbd2021.ssbd06.auth.endpoints;

import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AuthValidationException;
import pl.lodz.p.it.ssbd2021.ssbd06.security.JWTGenerator;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.AbstractEndpoint;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;

@Log
@Stateful
public class AuthEndpoint extends AbstractEndpoint implements AuthEndpointLocal {

    @Inject JWTGenerator jwtGenerator;

    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Override
    public String login(String login, String password) throws AppBaseException {
        Credential credential = new UsernamePasswordCredential(login, new Password(password));
        CredentialValidationResult result = identityStoreHandler.validate(credential);
        if(result.getStatus() == CredentialValidationResult.Status.VALID) {
            return jwtGenerator.generateJWTString(result);
        } else {
            throw AuthValidationException.invalidCredentials();
        }
    }

    /**
     * Zapisuje w dzienniku zdarzeń fakt wylogowanie użytkownika
     */
    @Override
    public void logout() {
        log.info(String.format("User %s was logged out", getLogin()));
    }
}
