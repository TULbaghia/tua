import React from "react";
import '../ModifyHotelWrapper.scss';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import i18n from "../../../i18n";
import {validatorFactory, ValidatorType} from "../../Validation/Validators";

export const ModifyHotelValidationSchema = values => {
    const errors = {};
    if (!values.name) {
        errors.name = i18n.t('modifyHotel.error.name_required');
    } else {
        validatorFactory(values.name, ValidatorType.HOTEL_NAME).forEach(x => {
            errors.name = x;
        })
    }

    if (!values.address) {
        errors.address = i18n.t('modifyHotel.error.address_required');
    } else {
        validatorFactory(values.address, ValidatorType.ADDRESS).forEach(x => {
            errors.address = x;
        })
    }

    if (!values.city) {
        errors.city = i18n.t('modifyHotel.error.city_required');
    }
    return errors;
}
