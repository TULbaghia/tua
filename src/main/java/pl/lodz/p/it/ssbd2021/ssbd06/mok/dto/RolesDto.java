package pl.lodz.p.it.ssbd2021.ssbd06.mok.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd06.security.Signable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa DTO reprezentująca role w systemie przypisane do użytkownika
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RolesDto implements Signable {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Long userId;

    private List<SingleRoleDto> rolesGranted;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<SingleRoleDto> rolesRevoked;

    @Override
    public String getMessageToSign() {
        return String.format("%d;%s;%s", userId, stringify(rolesGranted), stringify(rolesRevoked));
    }

    private static String stringify(List<SingleRoleDto> list) {
        return list.stream().map(x -> x.roleName + ":" + x.version).collect(Collectors.joining("|"));
    }

    /**
     * Klasa DTO reprezentująca rolę w systemie, przypisaną do użytkownika
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class SingleRoleDto {
        private String roleName;
        private Long version;
    }
}
