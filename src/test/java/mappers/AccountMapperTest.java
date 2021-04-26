package mappers;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mapstruct.factory.Mappers;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.ManagerData;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IAccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IRoleMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.ManagerDataDto;
import java.util.Date;

public class AccountMapperTest {
    private Account account;
    private final IAccountMapper mapper = Mappers.getMapper(IAccountMapper.class);

    @Before
    public void init(){
        account = new Account();
        account.setLogin("adam231@gmail.com");
        account.setFirstname("Adam");
        account.setLastname("Kowalski");
        account.setContactNumber("123456789");
        account.setEnabled(true);
        account.setConfirmed(true);
        account.setLastFailedLoginDate(new Date());
        account.setLastSuccessfulLoginDate(new Date());
    }

    @Test
    public void accountToAccountDtoTest(){
        AccountDto accountDto = mapper.accountToAccountDto(account);

        Assertions.assertEquals(account.getLogin(), accountDto.getLogin());
        Assertions.assertEquals(account.getFirstname(), accountDto.getFirstname());
        Assertions.assertEquals(account.getLastname(), accountDto.getLastname());
        Assertions.assertEquals(account.getContactNumber(), accountDto.getContactNumber());
        Assertions.assertEquals(account.getLastFailedLoginDate(), accountDto.getLastFailedLoginDate());
        Assertions.assertEquals(account.isConfirmed(), accountDto.isConfirmed());
    }

    @Test
    public void accountDtoToAccountTest(){
        AccountDto accountDto = mapper.accountToAccountDto(account);
        Account account2 = mapper.accountDtoToAccount(accountDto);

        Assertions.assertEquals(account.getLogin(), account2.getLogin());
        Assertions.assertEquals(account.getFirstname(), account2.getFirstname());
        Assertions.assertEquals(account.getLastname(), account2.getLastname());
        Assertions.assertEquals(account.getContactNumber(), account2.getContactNumber());
        Assertions.assertEquals(account.getLastFailedLoginDate(), account2.getLastFailedLoginDate());
        Assertions.assertEquals(account.isConfirmed(), account2.isConfirmed());
    }
}
