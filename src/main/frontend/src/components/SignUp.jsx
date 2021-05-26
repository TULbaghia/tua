import React, {useState} from "react";
import {useHistory} from "react-router";
import {useLocale} from "./LoginContext";
import {withNamespaces} from 'react-i18next';
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import {api} from "../Api";
import "../css/input.css";

import ReCAPTCHA from "react-google-recaptcha";
import {handleRecaptcha, recaptchaCheck} from "./Recaptcha/RecaptchaCallback";
import {useNotificationDangerAndLong, useNotificationWarningAndLong,} from "./Notification/NotificationProvider";
import {ResponseErrorHandler} from "./Validation/ResponseErrorHandler";
import {validatorFactory, ValidatorType} from "./Validation/Validators";
import { Formik, Form } from "formik";
import GridItemInput from "./controls/GridInputItem"

function SignUp(props) {
    const {t, i18n} = props
    const dispatchNotificationDanger = useNotificationDangerAndLong();
    const dispatchNotificationWarning = useNotificationWarningAndLong();
    const history = useHistory();
    const recaptchaRef = React.createRef();

    const initialValues = {
        login: '',
        email: '',
        password: '',
        repeatedPassword: '',
        firstname: '',
        lastname: '',
        contactNumber: ''
    }

    function onSubmit(values, helpers){
        if(!recaptchaCheck(recaptchaRef, dispatchNotificationWarning)) return

        const {repeatedPassword, ...dto} = values
        api.registerAccount(
            dto
        )
        .then(res => console.log(res))
        .catch(err => ResponseErrorHandler(err, dispatchNotificationDanger))
    }

    function validate(values){
        const errors = {}
        const loginErrors = validatorFactory(values.login, ValidatorType.LOGIN)
        if(loginErrors.length !== 0)
            errors.login = loginErrors
        const passwordErrors = validatorFactory(values.password, ValidatorType.PASSWORD)
        if(passwordErrors.length !== 0)
            errors.password = passwordErrors
        if(values.password !== values.repeatedPassword){
            errors.repeatedPassword = [t("passwordsNotMatch")]
        }
        const emailErrors = validatorFactory(values.email, ValidatorType.USER_EMAIL)
        if(emailErrors.length !== 0)
            errors.email = emailErrors
        const firstnameErrors = validatorFactory(values.firstname, ValidatorType.FIRSTNAME)
        if(firstnameErrors.length !== 0)
            errors.firstname = firstnameErrors
        const lastnameErrors = validatorFactory(values.lastname, ValidatorType.LASTNAME)
        if(lastnameErrors.length !== 0)
            errors.lastname = lastnameErrors
        const contactNumberErrors = validatorFactory(values.contactNumber, ValidatorType.CONTACT_NUMBER)
        if(contactNumberErrors.length !== 0)
            errors.contactNumber = contactNumberErrors
        return errors
    }

    return (
        <Formik {...{initialValues, validate, onSubmit}}>
            {({handleSubmit}) => (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('signUp')}</li>
            </BreadCrumb>
            <div className="floating-box pt-2 pb-2">
            <h1 className="h3">{t('registering')}</h1>

                <Form className="row g-3">
                    <GridItemInput name="login" placeholder={t("login")} type="text" />
                    <GridItemInput name="email" placeholder={t("emailAddress")} type="email" />
                    <GridItemInput name="password" placeholder={t("password")} type="password" />
                    <GridItemInput name="repeatedPassword" placeholder={t("repeatPassword")} type="password" />
                    <GridItemInput name="firstname" placeholder={t("name")} type="text" />
                    <GridItemInput name="lastname" placeholder={t("surname")} type="text" />
                    <GridItemInput name="contactNumber" placeholder={t("phoneNumber")} type="text" />
                    <div className="col-md-6">
                        <div style={{color: "#7749F8", fontSize: 14, marginBottom: "1rem"}}>
                            {t('obligatoryFields')}
                        </div>
                    </div>
                    
                    <div className="col-12">
                        <ReCAPTCHA style={{display: "inline-block"}} hl={i18n.language} ref={recaptchaRef} sitekey={process.env.REACT_APP_RECAPTCHA_SITE_KEY}/> 
                    </div>

                    <div className="col-12">
                        <button
                            className="btn btn-primary"
                            style={{backgroundColor: "#7749F8"}}
                            type="submit"
                        >
                            {t('signUp')}
                        </button>
                    </div>
               
                </Form>
            </div>
        </div>
        )}
        </Formik>

    );

}

export default withNamespaces()(SignUp);