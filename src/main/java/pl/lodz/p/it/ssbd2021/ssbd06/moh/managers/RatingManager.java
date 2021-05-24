package pl.lodz.p.it.ssbd2021.ssbd06.moh.managers;

import pl.lodz.p.it.ssbd2021.ssbd06.entities.Rating;
import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.RatingDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.enums.RatingVisibility;
import pl.lodz.p.it.ssbd2021.ssbd06.utils.common.LoggingInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

/**
 * Manager odpowiadający za zarządzanie ocenami hoteli.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class RatingManager {
    /**
     * Zwraca listę ocen hotelu
     *
     * @param hotelId identyfikator hotelu
     * @throws AppBaseException podczas błędu związanego z bazą danych
     * @return lista ocen hotelu
     */
    List<Rating> getAll(Long hotelId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje ocene
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void addRating(RatingDto ratingDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Modyfikuje ocenę
     *
     * @param ratingDto dto z danymi oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void updateRating(RatingDto ratingDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Usuwa ocenę hotelu
     *
     * @param ratingId identyfikator oceny
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void deleteRating(Long ratingId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zmień widoczność oceny
     *
     * @param ratingId dto z danymi hotelu
     * @param ratingVisibility poziom widoczności
     * @throws AppBaseException podczas błędu związanego z bazą danych
     */
    void changeVisibility(Long ratingId, RatingVisibility ratingVisibility) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
