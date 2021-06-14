import i18n from "i18next";
import {validatorFactory, ValidatorType} from "../../Validation/Validators";

export const AddBoxValidationSchema = (values) => {
    const errors = {};
    if (!values.price) {
        errors.name = i18n.t('addBox.error.price_required');
    } else {
        validatorFactory(values.price, ValidatorType.PRICE).forEach(x => {
            errors.name = x;
        })
    }

    return errors;
}