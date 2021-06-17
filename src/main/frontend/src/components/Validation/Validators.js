import React from 'react';
import i18n from "../../i18n";

const sizeValidator = (data, minInclusive, maxInclusive) => {
    if (data.length < minInclusive) {
        return i18n.t("form.validation.field.min_size");
    }
    if (data.length > maxInclusive) {
        return i18n.t("form.validation.field.max_size");
    }
}

const patternValidator = (data, pattern) => {
    if (!pattern.test(data)) {
        return i18n.t("form.validation.field.pattern");
    }
}

const allowedListValidator = (data, list) => {
    if (!list.includes(data)) {
        return i18n.t("form.validation.field.not_allowed");
    }
}

export const validateContactNumber = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 9, 15));
    errors.push(patternValidator(data, /^[0-9\+][0-9]{8,14}$/));
    return errors.filter(err => err !== undefined);
}

export const validateFirstname = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 3, 31));
    errors.push(patternValidator(data, /^[A-ZĆŁÓŚŹŻ\s]{1}[a-ząęćńóśłźż]+$/));
    return errors.filter(err => err !== undefined);
}

export const validateLanguage = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 2));
    errors.push(patternValidator(data, /^[a-z]{2}$/));
    errors.push(allowedListValidator(data, ["pl", "en"]));
    return errors.filter(err => err !== undefined);
}

export const validateLastname = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 31));
    errors.push(patternValidator(data, /^[A-ZĆŁÓŚŹŻ\s]{1}[a-ząęćńóśłźż]+$/));
    return errors.filter(err => err !== undefined);
}

export const validateLogin = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 3, 19));
    errors.push(patternValidator(data, /^[a-zA-Z0-9]+$/));
    return errors.filter(err => err !== undefined);
}

export const validatePassword = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 8, 64));
    errors.push(patternValidator(data, /^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,64}$/));
    return errors.filter(err => err !== undefined);
}

export const validatePenCode = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 32, 32));
    errors.push(patternValidator(data, /^[0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12}$/));
    return errors.filter(err => err !== undefined);
}

export const validateUserEmail = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 6, 127));
    errors.push(patternValidator(data, /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/));
    return errors.filter(err => err !== undefined);
}

export const validateHotelName = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 31));
    errors.push(patternValidator(data, /^[A-ZĆŁÓŚŹŻ\s]{1}[A-Za-ząęćńóśłźż \-]+$/));
    return errors.filter(err => err !== undefined);
}

export const validateAddress = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 63));
    errors.push(patternValidator(data, /^[A-Za-z0-9ĆŁÓŚŹŻąęćńóśłźż\s/]+$/));
    return errors.filter(err => err !== undefined);
}

export const validateHotelImage = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 127));
    errors.push(patternValidator(data, /^(\b(https?|ftp|file):\/\/)?[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$/));
    return errors.filter(err => err !== undefined);
}

export const validateHotelDescription = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 8, 511));
    errors.push(patternValidator(data, /^[A-Za-z0-9ĄĘĆŃÓŚŁŹŻąęćńóśłźż.,:\s\-]+$/));
    return errors.filter(err => err !== undefined);
}

export const validateCityName = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 2, 31));
    errors.push(patternValidator(data, /^[A-ZĆŁÓŚŹŻ\s]{1}[A-Za-ząęćńóśłźż \-]+$/));
    return errors.filter(err => err !== undefined);
}

export const validateCityDescription = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 8, 511));
    errors.push(patternValidator(data, /^[A-Za-z0-9ĄĘĆŃÓŚŁŹŻąęćńóśłźż.,:\s\-]+$/));
    return errors.filter(err => err !== undefined);
}

export const validatePrice = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 1, 8));
    errors.push(patternValidator(data, /^(\d*\.)?\d+$/));
    return errors.filter(err => err !== undefined);
}

export const validateBoxDescription = (data) => {
    let errors = [];
    errors.push(sizeValidator(data, 1, 31));
    errors.push(patternValidator(data, /^[A-Za-z0-9ĄĘĆŃÓŚŁŹŻąęćńóśłźż.,:\s\-]+$/));
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
    BOX_DESCRIPTION: "BOX_DESCRIPTION"
};

export const validatorFactory = (data, validatorType) => {
    switch (validatorType) {
        case ValidatorType.CONTACT_NUMBER:
            return validateContactNumber(data);
        case ValidatorType.FIRSTNAME:
            return validateFirstname(data);
        case ValidatorType.LANGUAGE:
            return validateLanguage(data);
        case ValidatorType.LASTNAME:
            return validateLastname(data);
        case ValidatorType.LOGIN:
            return validateLogin(data);
        case ValidatorType.PASSWORD:
            return validatePassword(data);
        case ValidatorType.PEN_CODE:
            return validatePenCode(data);
        case ValidatorType.USER_EMAIL:
            return validateUserEmail(data);
        case ValidatorType.HOTEL_NAME:
            return validateHotelName(data);
        case ValidatorType.ADDRESS:
            return validateAddress(data);
        case ValidatorType.HOTEL_IMAGE:
            return validateHotelImage(data);
        case ValidatorType.HOTEL_DESCRIPTION:
            return validateHotelDescription(data);
        case ValidatorType.CITY_NAME:
            return validateCityName(data);
        case ValidatorType.CITY_DESCRIPTION:
            return validateCityDescription(data);
        case ValidatorType.PRICE:
            return validatePrice(data);
        case ValidatorType.BOX_DESCRIPTION:
            return validateBoxDescription(data);
        default:
            return [];
    }
}
