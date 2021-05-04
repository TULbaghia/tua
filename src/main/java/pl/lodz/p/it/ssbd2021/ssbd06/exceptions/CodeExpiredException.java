package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

public class CodeExpiredException extends AppBaseException{
    public CodeExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeExpiredException(String message) {
        super(message);
    }
}
