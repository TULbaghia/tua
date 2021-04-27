package pl.lodz.p.it.ssbd2021.ssbd06.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.AdminData;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.ClientData;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.ManagerData;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AdminDataDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.ClientDataDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.ManagerDataDto;

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
}
