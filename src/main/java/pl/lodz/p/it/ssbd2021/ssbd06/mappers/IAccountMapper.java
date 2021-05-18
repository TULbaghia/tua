package pl.lodz.p.it.ssbd2021.ssbd06.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.*;

@Mapper
public interface IAccountMapper {

    AccountDto toAccountDto(Account account);

    AccountPersonalDetailsDto toAccountPersonalDetailsDto(Account account);
    void toAccount(AccountPersonalDetailsDto detailsDto, @MappingTarget Account account) throws AppBaseException;

    PasswordChangeDto toPasswordChangeDto(Account account);

    RegisterAccountDto toRegisterAccountDto(Account account);
    void toAccount(RegisterAccountDto registerAccountDto, @MappingTarget Account account);
}
