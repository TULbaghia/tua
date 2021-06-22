import React from "react";
import './ModifyCityWrapper.scss';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import {api} from "../../Api";

export const getCity = (id, token) => {
    return api.getCity(id, {
        headers: {
            Authorization: token,
        }
    });
};

export const modifyCity = ({values, token, etag}) => {
    return api.updateCity({
        id: values.id,
        name: values.name,
        description: values.description
    }, {
        headers: {
            Authorization: token,
            "Content-Type": "application/json",
            "If-Match": etag
        }
    });
};

export const deleteCity = ({id, token, etag}) => {
    return api.deleteCity(id, {
        headers: {
            Authorization: token,
            "If-Match": etag
        }
    });
};





