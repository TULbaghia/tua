package pl.lodz.p.it.ssbd2021.ssbd06.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.*;

@Mapper
public interface IAccountMapper {

    AccountDto toAccountDto(Account account);
    Account toAccount(AccountDto accountDto);

    AccountPersonalDetailsDto toAccountPersonalDetailsDto(Account account);
    void toAccount(AccountPersonalDetailsDto detailsDto, @MappingTarget Account account) throws AppBaseException;

    PasswordChangeDto toPasswordChangeDto(Account account);
    Account toAccount(PasswordChangeDto passwordChangeDto);

    RegisterAccountDto toRegisterAccountDto(Account account);
    Account toAccount(RegisterAccountDto registerAccountDto);
}
