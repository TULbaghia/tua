import React from "react";
import i18n from "../../../i18n";
import {validatorFactory, ValidatorType} from "../../Validation/Validators";

export const AddCityValidationSchema = values => {
    const errors = {};
    if (!values.name) {
        errors.name = i18n.t('addCity.error.name_required');
    } else {
        validatorFactory(values.name, ValidatorType.CITY_NAME).forEach(x => {
            errors.name = x;
        })
    }

    if (!values.description) {
        errors.description = i18n.t('addCity.error.description_required');
    } else {
        validatorFactory(values.description, ValidatorType.CITY_DESCRIPTION).forEach(x => {
            errors.description = x;
        })
    }

    return errors;
}
