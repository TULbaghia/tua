package pl.lodz.p.it.ssbd2021.ssbd06.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.lodz.p.it.ssbd2021.ssbd06.entities.*;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.mok.dto.*;

/**
 * Odpowiada za przeprowadzenie mapowania z obiektów klasy Account na klasy DTO, oraz z obiektów klas DTO na obiekty klasy Account
 */
@Mapper
public interface IAccountMapper {

    /**
     * Dokonuje mapowania z obiektu Account na obiekt AccountDto
     * @param account obiekt klasy Account
     * @return zmapowany obiekt klasy AccountDto
     */
    AccountDto toAccountDto(Account account);

    /**
     * Dokonuje mapowania z obiektu Account na obiekt AccountPersonalDetailsDto
     * @param account obiekt klasy Account
     * @return zmapowany obiekt klasy AccountPersonalDetailsDto
     */
    AccountPersonalDetailsDto toAccountPersonalDetailsDto(Account account);

    /**
     * Dokonuje mapowania z obiektu AccountPersonalDetailsDto na obiekt Account
     * @param detailsDto obiekt klasy AccountPersonalDetailsDto
     * @param account obiekt klasy Account na który przeprowadzamy mapowanie
     * @throws AppBaseException gdy nie udało się przeprowadzić operacji mapowania
     */
    void toAccount(AccountPersonalDetailsDto detailsDto, @MappingTarget Account account) throws AppBaseException;

    /**
     * Dokonuje mapowania z obiektu Account na obiekt PasswordChangeDto
     * @param account obiekt klasy Account
     * @return zmapowany obiekt klasy PasswordChangeDto
     */
    PasswordChangeDto toPasswordChangeDto(Account account);

    /**
     * Dokonuje mapowania z obiektu Account na obiekt RegisterAccountDto
     * @param account obiekt klasy Account
     * @return zmapowany obiekt klasy RegisterAccountDto
     */
    RegisterAccountDto toRegisterAccountDto(Account account);

    /**
     * Dokonuje mapowania z obiektu RegisterAccountDto na obiekt Account
     * @param registerAccountDto obiekt klasy RegisterAccountDto
     * @param account obiekt klasy Account na który przeprowadzamy mapowanie
     */
    void toAccount(RegisterAccountDto registerAccountDto, @MappingTarget Account account);
}
