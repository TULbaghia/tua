package validation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountPersonalDetailsDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class AccountValidationTest {
    private static Validator VALIDATOR;

    @BeforeClass
    public void init(){
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void nonValidEmail(){
        AccountPersonalDetailsDto accountPersonalDetailsDto = new AccountPersonalDetailsDto("marek",
                "Adrian", "Duda", "123456789");
        Set<ConstraintViolation<AccountPersonalDetailsDto>> cons = VALIDATOR.validate(accountPersonalDetailsDto);
        Assert.assertEquals(cons.size(), 2);

        AccountPersonalDetailsDto account2 = new AccountPersonalDetailsDto("marek123o2", "Marek",
                "Lewarek", "123456789");
        cons = VALIDATOR.validate(account2);
        Assert.assertEquals(cons.size(), 1);
    }

    @Test
    public void nonValidFirstnameAndLastname(){
        AccountPersonalDetailsDto account = new AccountPersonalDetailsDto("marek1@o2.pl", "marek",
                "L", "123456789");
        Set<ConstraintViolation<AccountPersonalDetailsDto>> cons = VALIDATOR.validate(account);
        Assert.assertEquals(cons.size(), 2);

        AccountPersonalDetailsDto account2 = new AccountPersonalDetailsDto("marek1@o2.pl", "Ma",
                "Lewarekowskilewarkiewiczowskilewandowskilambert", "123456789");
        cons = VALIDATOR.validate(account2);
        Assert.assertEquals(cons.size(), 2);
    }

    @Test
    public void nonValidContactNumber(){
        AccountPersonalDetailsDto account = new AccountPersonalDetailsDto("marek1@o2.pl", "Marek",
                "Lewarek", "3456789");
        Set<ConstraintViolation<AccountPersonalDetailsDto>> cons = VALIDATOR.validate(account);
        Assert.assertEquals(cons.size(), 1);

        AccountPersonalDetailsDto account2 = new AccountPersonalDetailsDto("marek1@o2.pl", "Marek",
                "Lewarek", "123456Z89");
        cons = VALIDATOR.validate(account2);
        Assert.assertEquals(cons.size(), 1);
    }
}
