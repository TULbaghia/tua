import React from 'react';
import i18n from "../../i18n";
import {dialogDuration} from "../Notification/Notification";

export const ResponseErrorHandler = (error, errorNotifier, shouldDispatchViolations = true, callbackAfter = ((x) => {})) => {
    let errorHandled = false;
    if (error.response.status === 500) {
        errorNotifier({
            "dialogDuration": dialogDuration.SECOND,
            "message": i18n.t("request.error.5xx")
        });
    }
    if (error.response.status === 404) {
        errorNotifier({
            "dialogDuration": dialogDuration.SECOND,
            "message": i18n.t("request.error.4xx")
        });
    }
    const responseData = error.response.data;

    if (responseData) {
        if (responseData.message === "error.rest.validation") {
            if (shouldDispatchViolations) {
                dispatchErrors(error, errorNotifier)
            }
        } else if (responseData.message !== undefined) {
            errorHandled = true;
            errorNotifier({
                "message": i18n.t(responseData.message)
            })
        } else {
            errorNotifier({
                "topic": i18n.t("request.exception.unknown_title"),
                "message": i18n.t(responseData)
            })
        }
    }

    callbackAfter(error);

    return errorHandled;
}

export const isValidationConstraintException = (error) => {
    const responseData = error.response.data;
    return responseData !== undefined && responseData.constraints !== undefined && responseData.message === "error.rest.validation";
}

export const dispatchErrors = (error, dispatchDanger) => {
    if (isValidationConstraintException(error)) {
        for (const [key, value] of Object.entries(error.response.data.constraints)) {
            value.forEach(x => {
                dispatchDanger({
                    message: i18n.t(x.replaceAll('{', '').replaceAll('}', ''))
                });
            })
        }
    }
}