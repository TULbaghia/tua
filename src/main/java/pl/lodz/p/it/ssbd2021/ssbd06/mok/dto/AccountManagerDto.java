package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Firstname;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Lastname;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.Login;

/**
 * Klasa DTO zawierająca informacje o koncie użytkownika z rolą Manager
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountManagerDto implements Signable {
    @Login
    private String login;
    @Firstname
    private String firstname;
    @Lastname
    private String lastname;

    @Override
    public String getMessageToSign() {
        return String.format("%s;%d", login);
    }
}
