package pl.lodz.p.it.ssbd2021.ssbd06.security;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 * Klasa odpowiadająca za uwierzytelnienie użytkownika
 */
public class AuthenticationIdentityStore implements IdentityStore {
    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {

        }
        return CredentialValidationResult.INVALID_RESULT;
    }
}
