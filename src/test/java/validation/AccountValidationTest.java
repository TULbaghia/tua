package validation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.AccountPersonalDetailsDto;
import pl.lodz.p.it.ssbd2021.ssbd06.validation.RegularExpression;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountValidationTest {
    private static Validator VALIDATOR;

    @BeforeClass
    public void init(){
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void validDtoShouldSuccess(){
        AccountPersonalDetailsDto accountPersonalDetailsDto = new AccountPersonalDetailsDto("Adrian",
                "Duda", "515789642");
        Set<ConstraintViolation<AccountPersonalDetailsDto>> cons = VALIDATOR.validate(accountPersonalDetailsDto);
        Assert.assertEquals(cons.size(), 0);
    }

    @Test
    public void nonValidFirstnameAndLastname(){
        AccountPersonalDetailsDto account = new AccountPersonalDetailsDto("marek",
                "L", "123456789");
        Set<ConstraintViolation<AccountPersonalDetailsDto>> cons = VALIDATOR.validate(account);
        Assert.assertEquals(cons.size(), 3);

        AccountPersonalDetailsDto account2 = new AccountPersonalDetailsDto("Ma",
                "Lewarekowskilewarkiewiczowskilewandowskilambert", "123456789");
        cons = VALIDATOR.validate(account2);
        Assert.assertEquals(cons.size(), 2);
    }

    @Test
    public void nonValidContactNumber(){
        AccountPersonalDetailsDto account = new AccountPersonalDetailsDto("Marek",
                "Lewarek", "3456789");
        Set<ConstraintViolation<AccountPersonalDetailsDto>> cons = VALIDATOR.validate(account);
        Assert.assertEquals(cons.size(), 2);

        AccountPersonalDetailsDto account2 = new AccountPersonalDetailsDto("Marek",
                "Lewarek", "123456Z89");
        cons = VALIDATOR.validate(account2);
        Assert.assertEquals(cons.size(), 1);
    }

    @Test
    public void differentKindsOfPhoneNumbersTest(){
        Pattern pattern = Pattern.compile(RegularExpression.CONTACT_NUMBER);
        Matcher matcher = pattern.matcher("+48132343456");
        Assert.assertTrue(matcher.matches());

        Matcher matcher1 = pattern.matcher("0481234567");
        Assert.assertTrue(matcher1.matches());
    }
}
