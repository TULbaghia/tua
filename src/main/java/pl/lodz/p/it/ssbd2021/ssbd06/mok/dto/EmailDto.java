package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.UserEmail;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailDto {
    @NotNull
    @UserEmail
    private String newEmail;
}
