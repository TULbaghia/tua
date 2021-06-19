import {api} from "../../Api";
import {rolesConstant} from "../../Constants";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";

export const getReservation = ({id, token}) => {
    return api.getBooking(id, {
        headers: {
            Authorization: token
        }
    });
}

export const getHotel = ({id, token}) => {
    return api.getHotel(id, {
        headers: {
            Authorization: token
        }
    });
}

export const getBox = ({id, token}) => {
    return api.getBox(id, {
        headers: {
            Authorization: token
        }
    });
}

export const getRenter = ({login, token, currentRole}) => {
    if (currentRole === rolesConstant.client) {
        return api.showAccountInformation({
            headers: {
                Authorization: token
            }
        });
    }
    return api.showAccount(login, {
        headers: {
            Authorization: token
        }
    });
}

export const apiCancelBooking = ({id, token, ETag, dispatchDanger}) => {
    return api.cancelBooking(id, {
        headers: {
            Authorization: token,
            "If-Match": ETag,
        }
    });
}

export const apiInProgressBooking = ({id, token, ETag, dispatchDanger}) => {
    return api.inProgressBooking(id, {
        headers: {
            Authorization: token,
            "If-Match": ETag,
        }
    });
}

export const apiEndBooking = ({id, token, ETag, dispatchDanger}) => {
    return api.endBooking(id, {
        headers: {
            Authorization: token,
            "If-Match": ETag,
        }
    })
}

export const apiGetRating = ({id, token}) => {
    return api.getRating(id, {
        headers: {
            Authorization: token
        }
    });
}