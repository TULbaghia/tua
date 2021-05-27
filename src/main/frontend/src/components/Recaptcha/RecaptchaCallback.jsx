import React from "react";
import i18n from "../../i18n";

export const handleRecaptcha = (onSuccessAction, recaptchaRef, dispatchNotification, onFailureAction=(() => {})) => {
    if (recaptchaRef.current.getValue() === "") {
        dispatchNotification({message: i18n.t('recaptchaInvalid')});
        onFailureAction();
    } else {
        onSuccessAction();
        window.grecaptcha.reset();
    }
}
