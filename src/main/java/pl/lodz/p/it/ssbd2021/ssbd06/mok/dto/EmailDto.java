package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.UserEmail;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailDto {
    @UserEmail
    private String newEmail;
}
