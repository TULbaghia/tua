package pl.lodz.p.it.ssbd2021.ssbd06.validation;

/**
 * Klasa zawierająca wyrażenia regularne.
 */
public class RegularExpression {
    public static final String PASSWORD = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,64}$";
    public static final String FIRSTNAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[a-ząęćńóśłźż]+$";
    public static final String LASTNAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[a-ząęćńóśłźż]+$";
    public static final String CONTACT_NUMBER = "^[0-9\\+][0-9]{8,14}$";
    public static final String LANGUAGE_CODE = "[a-z]{2}";
    public static final String LOGIN = "^[a-zA-Z0-9]+$";
    public static final String PENDING_CODE = "^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$";
}
