import {api} from "../../Api";

export const getBox = (id, token) => {
    return api.getBox(id, {
        headers: {
            Authorization: token,
        }
    });
};

export const modifyBox = ({values, token, etag}) => {
    return api.updateBox({
        id: values.id,
        price: values.price,
        description: values.description
    }, {
        headers: {
            "Content-Type": "application/json",
            Authorization: token,
            "If-Match": etag
        }
    });
};