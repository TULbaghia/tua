import React from "react";
import {api} from "../../Api";

export const addCity = ({values, token}) => {
    return api.addCity({
        name: values.name,
        description: values.description
    }, {
        headers: {
            "Content-Type": "application/json",
            Authorization: token,
        }
    });
};







