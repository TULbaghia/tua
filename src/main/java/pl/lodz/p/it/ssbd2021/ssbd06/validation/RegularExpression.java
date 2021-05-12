package pl.lodz.p.it.ssbd2021.ssbd06.validation;

/**
 * Klasa zawierająca wyrażenia regularne.
 */
public class RegularExpression {
    public static final String FIRSTNAME = "^[A-ZŁŚŻ\\s]{1}[a-z,ą,ę,ć,ń,ó,ś,ł]+$";
    public static final String LASTNAME = "^[A-ZŁŚŻ\\s]{1}[a-z,ą,ę,ć,ń,ó,ś,ł]+$";
    public static final String CONTACT_NUMBER = "[0-9/+][0-9]{8,14}";
    public static final String LANGUAGE_CODE = "[a-z]{2}";
}
