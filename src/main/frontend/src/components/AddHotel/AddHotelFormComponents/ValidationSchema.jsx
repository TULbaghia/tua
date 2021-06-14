import React from "react";
import '../AddHotelWrapper.scss';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import i18n from "../../../i18n";
import {validatorFactory, ValidatorType} from "../../Validation/Validators";

export const AddHotelValidationSchema = values => {
    const errors = {};
    if (!values.name) {
        errors.name = i18n.t('addHotel.error.name_required');
    } else {
        validatorFactory(values.name, ValidatorType.HOTEL_NAME).forEach(x => {
            errors.name = x;
        })
    }

    if (!values.address) {
        errors.address = i18n.t('addHotel.error.address_required');
    } else {
        validatorFactory(values.address, ValidatorType.ADDRESS).forEach(x => {
            errors.address = x;
        })
    }

    if (!values.city) {
        errors.city = i18n.t('addHotel.error.city_required');
    }

    if (values.image) {
        validatorFactory(values.image, ValidatorType.HOTEL_IMAGE).forEach(x => {
            errors.image = x;
        })
    }

    if (!values.description) {
        errors.description = i18n.t('addHotel.error.description_required');
    } else {
        validatorFactory(values.description, ValidatorType.HOTEL_DESCRIPTION).forEach(x => {
            errors.description = x;
        })
    }

    return errors;
}
