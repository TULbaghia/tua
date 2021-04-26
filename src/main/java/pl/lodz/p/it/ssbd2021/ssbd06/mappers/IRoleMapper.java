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

@Mapper
public interface IRoleMapper {
    @Mappings({
            @Mapping(target = "hotelName", source = "hotel.name"),
            @Mapping(target = "login", source = "account.login")
    })
    ManagerDataDto managerDatatoManagerDataDto(ManagerData managerData);

    @Mapping(target = "login", source = "account.login")
    ClientDataDto clientDataToClientDataDto(ClientData clientData);

    @Mapping(target = "login", source = "account.login")
    AdminDataDto adminDataToAdminDataDto(AdminData adminData);
}
