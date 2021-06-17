import i18n from "i18next";
import {validatorFactory, ValidatorType} from "../../Validation/Validators";

export const AddBoxValidationSchema = values => {
    const errors = {};
    if (!values.price) {
        errors.price = i18n.t('box.error.price_required');
    } else {
        validatorFactory(values.price, ValidatorType.PRICE).forEach(x => {
            errors.price = x;
        })
    }

    if (!values.description) {
        errors.description = i18n.t('box.error.description_required');
    } else {
        validatorFactory(values.description, ValidatorType.BOX_DESCRIPTION).forEach(x => {
            errors.description = x;
        })
    }

    return errors;
}