package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;
import java.net.Inet4Address;
import java.util.Date;

/**
 * Klasa DTO zawierająca informacje o koncie użytkownika
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDto {
    private String login;
    private String firstName;
    private String lastName;
    private String language;
    private String contactNumber;
    private boolean enabled;
    private boolean confirmed;
    private Date lasSuccessfulLoginDate;
    private Inet4Address lastSuccessfulLoginIpAddress;
    private Date lastFailedLoginDate;
    private Inet4Address lastFailedLoginIpAddress;

}
