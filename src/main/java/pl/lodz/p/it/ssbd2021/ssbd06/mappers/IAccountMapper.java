package pl.lodz.p.it.ssbd2021.ssbd06.mappers;
import org.mapstruct.Mapper;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.*;

@Mapper
public interface IAccountMapper {

    AccountDto accountToAccountDto(Account account);
    Account accountDtoToAccount(AccountDto accountDto);

    AccountPersonalDetailsDto accountToAccountPersonalDetailsDto(Account account);
    Account accountPersonalDetailsDtoToAccount(AccountPersonalDetailsDto accountPersonalDetailsDto);

    PasswordChangeDto accountToPasswordChangeDto(Account account);
    Account passwordChangeDtoToAccount(PasswordChangeDto passwordChangeDto);

    RegisterAccountDto accountToRegisterAccountDto(Account account);
    Account registerAccountDtoToAccount(RegisterAccountDto registerAccountDto);
}
