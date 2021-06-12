import React from "react";
import './ModifyHotelWrapper.scss';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import {api} from "../../Api";

export const getOwnHotelEtag = (token) => {
    return api.getOwnHotelInfo({
        method: "GET",
        headers: {
            Authorization: token,
        }
    });
};

export const getOtherHotelEtag = (id, token) => {
    return api.getOtherHotelInfo(id, {
        method: "GET",
        headers: {
            Authorization: token,
        }
    });
};

export const getCities = (token) => {
    return api.getAllCities({
        method: "GET",
        headers: {
            Authorization: token,
        }});
};

export const modifyHotel = ({values, token, etag}) => {
    return api.updateOwnHotel({name: values.name, address: values.address, cityId: values.city}, {
        headers: {
            "Content-Type": "application/json",
            Authorization: token,
            "If-Match": etag
        }
    });
};

export const modifyOtherHotel = ({id, values, token, etag}) => {
    return api.updateOtherHotel(id, {name: values.name, address: values.address, cityId: values.city}, {
        headers: {
            "Content-Type": "application/json",
            Authorization: token,
            "If-Match": etag
        }
    });
};







