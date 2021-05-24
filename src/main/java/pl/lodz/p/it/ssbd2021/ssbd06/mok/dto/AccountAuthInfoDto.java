package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;
import java.util.Date;

/**
 * Klasa DTO zawierająca informacje dotyczące logowania użytkownika
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountAuthInfoDto {
    private String login;
    private Date lastSuccessfulLoginDate;
    private Date lastFailedLoginDate;
    private String lastSuccessfulLoginIpAddress;
    private String lastFailedLoginIpAddress;
}
