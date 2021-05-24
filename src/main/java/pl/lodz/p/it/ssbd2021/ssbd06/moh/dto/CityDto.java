package pl.lodz.p.it.ssbd2021.ssbd06.moh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto implements Signable {
    private Long id;
    private String name;
    private String description;

    private Long version;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%d", id, version);
    }
}
