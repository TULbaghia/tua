package pl.lodz.p.it.ssbd2021.ssbd06.validation;

/**
 * Klasa zawierająca wyrażenia regularne.
 */
public class RegularExpression {
    public static final String FIRSTNAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[a-z,ą,ę,ć,ń,ó,ś,ł,ź,ż]+$";
    public static final String LASTNAME = "^[A-ZĆŁÓŚŹŻ\\s]{1}[a-z,ą,ę,ć,ń,ó,ś,ł,ź,ż]+$";
    public static final String CONTACT_NUMBER = "[0-9/+][0-9]{8,14}";
    public static final String LANGUAGE_CODE = "[a-z]{2}";
}
