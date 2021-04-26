package pl.lodz.p.it.ssbd2021.ssbd06.mappers;
import org.mapstruct.Mapper;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.*;

@Mapper
public interface IAccountMapper {

    AccountDto toAccountDto(Account account);
    Account toAccount(AccountDto accountDto);

    AccountPersonalDetailsDto toAccountPersonalDetailsDto(Account account);
    Account toAccount(AccountPersonalDetailsDto accountPersonalDetailsDto);

    PasswordChangeDto toPasswordChangeDto(Account account);
    Account toAccount(PasswordChangeDto passwordChangeDto);

    RegisterAccountDto toRegisterAccountDto(Account account);
    Account toAccount(RegisterAccountDto registerAccountDto);
}
