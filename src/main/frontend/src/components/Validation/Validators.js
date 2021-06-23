import React from 'react';
import i18n from "../../i18n";

const sizeValidator = (data, minInclusive, maxInclusive) => {
    if (data.length < minInclusive) {
        return "form.validation.field.min_size";
    }
    if (data.length > maxInclusive) {
        return "form.validation.field.max_size";
    }
}

const patternValidator = (data, pattern) => {
    if (!pattern.test(data)) {
        return "form.validation.field.pattern";
    }
}

const allowedListValidator = (data, list) => {
    if (!list.includes(data)) {
        return "form.validation.field.not_allowed";
    }
}

const changeValidatorIdentity = (errors, identity) => {
    return errors.map(x => {
        const translationString = x.replace("field", "field." + identity.toLowerCase());
        const translated = i18n.t(translationString);
        if (translated === translationString) {
            return i18n.t(x);
        }
        return translated;
    });
}

const validateContactNumber = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 9, 15));
    errors.push(patternValidator(data, /^[0-9\+][0-9]{8,14}$/));
    return errors.filter(err => err !== undefined);
}

const validateFirstname = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 3, 31));
    errors.push(patternValidator(data, /^[A-ZĆŁÓŚŹŻ\s]{1}[a-ząęćńóśłźż]+$/));
    return errors.filter(err => err !== undefined);
}

const validateLanguage = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 2));
    errors.push(patternValidator(data, /^[a-z]{2}$/));
    errors.push(allowedListValidator(data, ["pl", "en"]));
    return errors.filter(err => err !== undefined);
}

const validateLastname = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 31));
    errors.push(patternValidator(data, /^[A-ZĆŁÓŚŹŻ\s]{1}[a-ząęćńóśłźż]+$/));
    return errors.filter(err => err !== undefined);
}

const validateLogin = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 3, 19));
    errors.push(patternValidator(data, /^[a-zA-Z0-9]+$/));
    return errors.filter(err => err !== undefined);
}

const validatePassword = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 8, 64));
    errors.push(patternValidator(data, /^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,64}$/));
    return errors.filter(err => err !== undefined);
}

const validatePenCode = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 32, 32));
    errors.push(patternValidator(data, /^[0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12}$/));
    return errors.filter(err => err !== undefined);
}

const validateUserEmail = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 6, 127));
    errors.push(patternValidator(data, /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/));
    return errors.filter(err => err !== undefined);
}

const validateHotelName = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 31));
    errors.push(patternValidator(data, /^[A-ZĆŁÓŚŹŻ\s]{1}[A-Za-ząęćńóśłźż \-]+$/));
    return errors.filter(err => err !== undefined);
}

const validateAddress = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 63));
    errors.push(patternValidator(data, /^[A-Za-z0-9ĆŁÓŚŹŻąęćńóśłźż\s/]+$/));
    return errors.filter(err => err !== undefined);
}

const validateHotelImage = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 31));
    errors.push(patternValidator(data, /^(?!\/\/)(\/[A-Za-z0-9]+)+\.(jpg|png)$/));
    return errors.filter(err => err !== undefined);
}

const validateHotelDescription = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 8, 511));
    errors.push(patternValidator(data, /^[A-Za-z0-9ĄĘĆŃÓŚŁŹŻąęćńóśłźż.,:\s\-]+$/));
    return errors.filter(err => err !== undefined);
}

const validateCityName = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 31));
    errors.push(patternValidator(data, /^[A-ZĆŁÓŚŹŻ\s]{1}[A-Za-ząęćńóśłźż \-]+$/));
    return errors.filter(err => err !== undefined);
}

const validateCityDescription = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 8, 511));
    errors.push(patternValidator(data, /^[A-Za-z0-9ĄĘĆŃÓŚŁŹŻąęćńóśłźż.,:\s\-]+$/));
    return errors.filter(err => err !== undefined);
}

const validatePrice = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 1, 8));
    errors.push(patternValidator(data, /^(\d*\.)?\d+$/));
    return errors.filter(err => err !== undefined);
}

const validateBoxDescription = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 1, 31));
    errors.push(patternValidator(data, /^[A-Za-z0-9ĄĘĆŃÓŚŁŹŻąęćńóśłźż.,:\s\-]+$/));
    return errors.filter(err => err !== undefined);
}

const validateRatingComment = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 4, 255));
    return errors.filter(err => err !== undefined);
}

export const ValidatorType = {
    CONTACT_NUMBER: "CONTACT_NUMBER",
    FIRSTNAME: "FIRSTNAME",
    LANGUAGE: "LANGUAGE",
    LASTNAME: "LASTNAME",
    LOGIN: "LOGIN",
    PASSWORD: "PASSWORD",
    PEN_CODE: "PEN_CODE",
    USER_EMAIL: "USER_EMAIL",
    HOTEL_NAME: "HOTEL_NAME",
    ADDRESS: "ADDRESS",
    HOTEL_IMAGE: "HOTEL_IMAGE",
    HOTEL_DESCRIPTION: "HOTEL_DESCRIPTION",
    CITY_NAME: "CITY_NAME",
    CITY_DESCRIPTION: "CITY_DESCRIPTION",
    PRICE: "PRICE",
    BOX_DESCRIPTION: "BOX_DESCRIPTION",
    RATING_COMMENT: "RATING_COMMENT"
};

export const validatorFactory = (data, validatorType) => {
    switch (validatorType) {
        case ValidatorType.CONTACT_NUMBER:
            return changeValidatorIdentity(validateContactNumber(data), 'CONTACT_NUMBER');
        case ValidatorType.FIRSTNAME:
            return changeValidatorIdentity(validateFirstname(data), 'FIRSTNAME');
        case ValidatorType.LANGUAGE:
            return changeValidatorIdentity(validateLanguage(data), 'LANGUAGE');
        case ValidatorType.LASTNAME:
            return changeValidatorIdentity(validateLastname(data), 'LASTNAME');
        case ValidatorType.LOGIN:
            return changeValidatorIdentity(validateLogin(data), 'LOGIN');
        case ValidatorType.PASSWORD:
            return changeValidatorIdentity(validatePassword(data), 'PASSWORD');
        case ValidatorType.PEN_CODE:
            return changeValidatorIdentity(validatePenCode(data), 'PEN_CODE');
        case ValidatorType.USER_EMAIL:
            return changeValidatorIdentity(validateUserEmail(data), 'USER_EMAIL');
        case ValidatorType.HOTEL_NAME:
            return changeValidatorIdentity(validateHotelName(data), 'HOTEL_NAME');
        case ValidatorType.ADDRESS:
            return changeValidatorIdentity(validateAddress(data), 'ADDRESS');
        case ValidatorType.HOTEL_IMAGE:
            return changeValidatorIdentity(validateHotelImage(data), 'HOTEL_IMAGE');
        case ValidatorType.HOTEL_DESCRIPTION:
            return changeValidatorIdentity(validateHotelDescription(data), 'HOTEL_DESCRIPTION');
        case ValidatorType.CITY_NAME:
            return changeValidatorIdentity(validateCityName(data), 'CITY_NAME');
        case ValidatorType.CITY_DESCRIPTION:
            return changeValidatorIdentity(validateCityDescription(data), 'CITY_DESCRIPTION');
        case ValidatorType.PRICE:
            return changeValidatorIdentity(validatePrice(data), 'PRICE');
        case ValidatorType.BOX_DESCRIPTION:
            return changeValidatorIdentity(validateBoxDescription(data), 'BOX_DESCRIPTION');
        case ValidatorType.RATING_COMMENT:
            return changeValidatorIdentity(validateRatingComment(data), 'RATING_COMMENT');
        default:
            return [];
    }
}
