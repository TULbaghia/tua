package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AdminDataDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.ClientDataDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.ManagerDataDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.RolesDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi", uses = ReferenceMapper.class)
public interface IRoleMapper {
    @Mappings({
            @Mapping(target = "hotelName", source = "hotel.name"),
            @Mapping(target = "id", source = "account.id")
    })
    ManagerDataDto toManagerDataDto(ManagerData managerData);

    @Mapping(target = "account", source = "id")
    ManagerData toManagerData(ManagerDataDto managerDataDto);

    @Mapping(target = "id", source = "account.id")
    ClientDataDto toClientDataDto(ClientData clientData);

    @Mapping(target = "account", source = "id")
    ClientData toClientData(ClientDataDto clientDataDto);

    @Mapping(target = "id", source = "account.id")
    AdminDataDto toAdminDataDto(AdminData adminData);

    @Mapping(target = "account", source = "id")
    AdminData toAdminData(AdminDataDto adminDataDto);

    @Mappings({
            @Mapping(target = "roleName", source = "accessLevel"),
            @Mapping(target = "version", source = "version")
    })
    RolesDto.SingleRoleDto toSingleRoleDto(Role role);

    default RolesDto toRolesDto(Account account) {
        List<RolesDto.SingleRoleDto> roleGranted = account.getRoleList().stream()
                .filter(Role::isEnabled)
                .map(this::toSingleRoleDto)
                .collect(Collectors.toList());
        List<RolesDto.SingleRoleDto> roleRevoked = account.getRoleList().stream()
                .filter(x -> !x.isEnabled())
                .map(this::toSingleRoleDto)
                .collect(Collectors.toList());
        return new RolesDto(account.getId(), roleGranted, roleRevoked);
    }
}
