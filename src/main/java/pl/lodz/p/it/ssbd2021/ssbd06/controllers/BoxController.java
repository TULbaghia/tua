package pl.lodz.p.it.ssbd2021.ssbd06.controllers;

import pl.lodz.p.it.ssbd2021.ssbd06.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.BoxDto;
import pl.lodz.p.it.ssbd2021.ssbd06.moh.dto.NewBoxDto;

import javax.ws.rs.*;
import java.util.List;

/**
 * Kontroler odpowiadający za zarządzanie klatkami.
 */
@Path("/boxes")
public class BoxController extends AbstractController {
    /**
     * Zwraca klatkę o podanym identyfikatorze
     *
     * @param id identyfikator klatki
     * @throws AppBaseException podczas błędu związanego z pobieraniem klatki
     * @return dto klatki
     */
    @GET
    @Path("/{id}")
    public BoxDto get(@PathParam("id") Long id) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca listę klatek
     *
     * @throws AppBaseException podczas błędu związanego z pobieraniem listy klatek
     * @return lista dto klatek
     */
    @GET
    public List<BoxDto> getAll() throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje klatkę
     *
     * @param boxDto dto z danymi nowej klatki
     * @throws AppBaseException podczas błędu związanego z dodaniem klatki
     */
    @POST
    public void addBox(NewBoxDto boxDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Modyfikuje klatkę
     *
     * @param boxDto dto z danymi klatki
     * @throws AppBaseException podczas błędu związanego z aktualizacją klatki
     */
    @PUT
    public void updateBox(BoxDto boxDto) throws AppBaseException {
        throw new UnsupportedOperationException();
    }

    /**
     * Usuwa klatkę
     *
     * @param boxId identyfikator klatki
     * @throws AppBaseException podczas błędu związanego z usunięciem klatki
     */
    @DELETE
    @Path("/{id}")
    public void deleteBox(@PathParam("id") Long boxId) throws AppBaseException {
        throw new UnsupportedOperationException();
    }
}
