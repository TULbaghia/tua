package pl.lodz.p.it.ssbd2021.ssbd06.auth.endpoints;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;

import javax.ejb.Local;

@Local
public interface AuthEndpointLocal {
    /**
     * Przeprowadza proces uwierzytelnienia użytkownika
     *
     * @param login  login użytkownika
     * @param password hasło użytkownika
     * @throws AppBaseException gdy uwierzytelnianie się nie powiedzie
     */
    void login(String login, String password) throws AppBaseException;
}
