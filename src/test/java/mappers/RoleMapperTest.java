package mappers;


import org.mapstruct.factory.Mappers;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Account;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.Hotel;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.ManagerData;
import pl.lodz.p.it.ssbd2021.ssbd06.mappers.IRoleMapper;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.ManagerDataDto;

import java.util.Date;

public class RoleMapperTest {
    private final IRoleMapper mapper = Mappers.getMapper(IRoleMapper.class);
    private ManagerData managerData;

    @BeforeTest
    public void init(){
        Account account = new Account();
        account.setLogin("adam231@gmail.com");
        account.setFirstname("Adam");
        account.setLastname("Kowalski");
        account.setContactNumber("123456789");
        account.setEnabled(true);
        account.setConfirmed(true);
        account.setLastFailedLoginDate(new Date());
        account.setLastSuccessfulLoginDate(new Date());

        managerData = new ManagerData();
        managerData.setAccount(account);
        managerData.setEnabled(true);
        managerData.setHotel(new Hotel("Qubus", "Mickiewicza 42", "Brak"));
    }

    @Test
    public void managerDataToManagerDataDtoTest() {
        ManagerDataDto managerDataDto = mapper.toManagerDataDto(managerData);

        Assert.assertEquals(managerData.getHotel().getName(), managerDataDto.getHotelName());
        Assert.assertEquals(managerData.getAccount().getId(), managerDataDto.getId());
        Assert.assertEquals(managerData.isEnabled(), managerDataDto.isEnabled());
    }
}
