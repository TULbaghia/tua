import React from "react";
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import i18n from "../../i18n";
import {validatorFactory, ValidatorType} from "../Validation/Validators";

export const NewRatingValidator = values => {
    const errors = {};

    if(values.comment) {
        validatorFactory(values.comment, ValidatorType.RATING_COMMENT).forEach(x => {
            errors.name = x;
        })
    }

    if(!values.rate) {
        errors.name = i18n.t('addRating.error.rating_requierd');
    }

    return errors;
}
