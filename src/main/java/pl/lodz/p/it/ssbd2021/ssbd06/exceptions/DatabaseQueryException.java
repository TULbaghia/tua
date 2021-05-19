package pl.lodz.p.it.ssbd2021.ssbd06.exceptions;

/**
 * Reprezentuje błąd pojawiający się podczas wykonywania operacji na bazie danych
 */
public class DatabaseQueryException extends AppBaseException {

    private static final String DATABASE_QUERY = "exception.database_query_exception.database_query_exception";

    private DatabaseQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    private DatabaseQueryException(String message) {
        super(message);
    }

    /**
     * Tworzy wyjątek reprezentujący problem z bazą danych.
     * @return wyjątek DatabaseQueryException
     */
    public static DatabaseQueryException databaseQueryException() {
        return new DatabaseQueryException(DATABASE_QUERY);
    }

    /**
     * Tworzy wyjątek reprezentujący problem z bazą danych.
     * @param cause wyjątek, który zostanie opakowany
     * @return wyjątek DatabaseQueryException
     */
    public static DatabaseQueryException databaseQueryException(Throwable cause) {
        return new DatabaseQueryException(DATABASE_QUERY, cause);
    }
}
