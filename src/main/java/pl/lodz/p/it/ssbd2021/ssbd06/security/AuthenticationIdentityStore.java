package pl.lodz.p.it.ssbd2021.ssbd06.security;

import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

/**
 * Klasa odpowiadająca za uwierzytelnienie użytkownika
 */
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:app/jdbc/ssbd06authDS",
        callerQuery = "SELECT DISTINCT password FROM auth_view WHERE login = ?",
        groupsQuery = "SELECT role FROM auth_view WHERE login = ?",
        hashAlgorithm = PasswordHasher.class
)
public class AuthenticationIdentityStore {

}
