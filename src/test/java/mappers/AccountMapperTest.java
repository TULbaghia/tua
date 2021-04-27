package mappers;

import org.mapstruct.factory.Mappers;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IAccountMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountDto;

import java.util.Date;

public class AccountMapperTest {
    private Account account;
    private final IAccountMapper mapper = Mappers.getMapper(IAccountMapper.class);

    @BeforeTest
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
        AccountDto accountDto = mapper.toAccountDto(account);

        Assert.assertEquals(account.getLogin(), accountDto.getLogin());
        Assert.assertEquals(account.getFirstname(), accountDto.getFirstname());
        Assert.assertEquals(account.getLastname(), accountDto.getLastname());
        Assert.assertEquals(account.getContactNumber(), accountDto.getContactNumber());
        Assert.assertEquals(account.getLastFailedLoginDate(), accountDto.getLastFailedLoginDate());
        Assert.assertEquals(account.isConfirmed(), accountDto.isConfirmed());
    }

    @Test
    public void accountDtoToAccountTest(){
        AccountDto accountDto = mapper.toAccountDto(account);
        Account account2 = mapper.toAccount(accountDto);

        Assert.assertEquals(account.getLogin(), account2.getLogin());
        Assert.assertEquals(account.getFirstname(), account2.getFirstname());
        Assert.assertEquals(account.getLastname(), account2.getLastname());
        Assert.assertEquals(account.getContactNumber(), account2.getContactNumber());
        Assert.assertEquals(account.getLastFailedLoginDate(), account2.getLastFailedLoginDate());
        Assert.assertEquals(account.isConfirmed(), account2.isConfirmed());
    }
}
