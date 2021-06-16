import React from "react";
import '../ModifyCityWrapper.scss';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import i18n from "../../../i18n";
import {validatorFactory, ValidatorType} from "../../Validation/Validators";

export const ModifyCityValidationSchema = values => {
    const errors = {};
    if (!values.name) {
        errors.name = i18n.t('modifyCity.error.name_required');
    } else {
        validatorFactory(values.name, ValidatorType.CITY_NAME).forEach(x => {
            errors.name = x;
        })
    }

    if (!values.description) {
        errors.description = i18n.t('modifyCity.error.description_required');
    } else {
        validatorFactory(values.description, ValidatorType.CITY_DESCRIPTION).forEach(x => {
            errors.description = x;
        })
    }

    return errors;
}
