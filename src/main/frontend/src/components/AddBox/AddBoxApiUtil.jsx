import {api} from "../../Api";

export const addBox = ({values, token}) => {
    return api.addBox({
        animalType: values.animalType,
        price: values.price,
        description: values.description
    }, {
        headers: {
            "Content-Type": "application/json",
            Authorization: token,
        }
    });
};