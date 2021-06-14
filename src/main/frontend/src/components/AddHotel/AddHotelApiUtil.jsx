import React from "react";
import './AddHotelWrapper.scss';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import {api} from "../../Api";

export const getCities = (token) => {
    return api.getAllCities({
        method: "GET",
        headers: {
            Authorization: token,
        }});
};

export const addHotel = ({values, token}) => {
    return api.addHotel({
        name: values.name,
        address: values.address,
        cityId: values.city,
        image: values.image ? values.image : null,
        description: values.description
    }, {
        headers: {
            "Content-Type": "application/json",
            Authorization: token,
        }
    });
};






