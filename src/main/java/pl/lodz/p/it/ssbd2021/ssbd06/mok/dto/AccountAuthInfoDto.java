package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;
import java.net.Inet4Address;
import java.util.Date;

/**
 * Klasa DTO zawierająca informacje dotyczące logowania użytkownika
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountAuthInfoDto {
    private String login;
    private Date lasSuccessfulLoginDate;
    private Date lastFailedLoginDate;
    private Inet4Address lastSuccessfulLoginIpAddress;
    private Inet4Address lastFailedLoginIpAddress;
}
