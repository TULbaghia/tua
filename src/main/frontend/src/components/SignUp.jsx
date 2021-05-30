import React, {useState} from "react";
import {useHistory} from "react-router";
import {withNamespaces} from 'react-i18next';
import BreadCrumb from "./Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {buildApi} from "../Api";
import ReCAPTCHA from "react-google-recaptcha";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort,
    useNotificationWarningAndLong
} from "./Utils/Notification/NotificationProvider";
import {recaptchaCheck} from "./Recaptcha/RecaptchaCallback";
import {ResponseErrorHandler} from "./Validation/ResponseErrorHandler";
import {validatorFactory, ValidatorType} from "./Validation/Validators";
import {useThemeColor} from "./Utils/ThemeColor/ThemeColorProvider";
import {v4} from "uuid";
import {Form, Formik} from "formik";
import GridItemInput from "./controls/GridInputItem"
import '../css/overlay.css'
import 'bootstrap/dist/css/bootstrap.css';

function SignUp(props) {
    const {t, i18n} = props
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchNotificationWarning = useNotificationWarningAndLong();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const history = useHistory();
    const [reCaptchaKey, setReCaptchaKey] = useState(v4());
    const [submitting, setSubmitting] = useState(false)
    const recaptchaRef = React.createRef();

    const colorTheme = useThemeColor();

    React.useEffect(() => {
        setReCaptchaKey(v4());
    }, [colorTheme, i18n.language])

    const initialValues = {
        login: '',
        email: '',
        password: '',
        repeatedPassword: '',
        firstname: '',
        lastname: '',
        contactNumber: ''
    }

    function onSubmit(values, {resetForm}) {
        if (!recaptchaCheck(recaptchaRef, dispatchNotificationWarning)) {
            setSubmitting(false)
            return
        }
        setSubmitting(true);

        const {repeatedPassword, ...dto} = values
        const api = buildApi(i18n.language);
        api.registerAccount(
            dto
        )
            .then(res => {
                dispatchNotificationSuccess({message: t('register.successfulEmailInfo')})
                resetForm()
            })
            .catch(err => {
                ResponseErrorHandler(err, dispatchNotificationDanger)
            }).finally(() => {
                setSubmitting(false)
            })
    }

    function validate(values) {
        const errors = {}
        const loginErrors = validatorFactory(values.login, ValidatorType.LOGIN)
        if (loginErrors.length !== 0)
            errors.login = loginErrors
        const passwordErrors = validatorFactory(values.password, ValidatorType.PASSWORD)
        if (passwordErrors.length !== 0)
            errors.password = passwordErrors
        if (values.password !== values.repeatedPassword) {
            errors.repeatedPassword = [t("passwordsNotMatch")]
        }
        const emailErrors = validatorFactory(values.email, ValidatorType.USER_EMAIL)
        if (emailErrors.length !== 0)
            errors.email = emailErrors
        const firstnameErrors = validatorFactory(values.firstname, ValidatorType.FIRSTNAME)
        if (firstnameErrors.length !== 0)
            errors.firstname = firstnameErrors
        const lastnameErrors = validatorFactory(values.lastname, ValidatorType.LASTNAME)
        if (lastnameErrors.length !== 0)
            errors.lastname = lastnameErrors
        const contactNumberErrors = validatorFactory(values.contactNumber, ValidatorType.CONTACT_NUMBER)
        if (contactNumberErrors.length !== 0)
            errors.contactNumber = contactNumberErrors
        return errors
    }

    return (
        <Formik {...{initialValues, validate, onSubmit, submitting}}>
            {({handleSubmit, isSubmitting}) => (
                <div className="container">
                    <BreadCrumb>
                        <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                        <li className="breadcrumb-item active" aria-current="page">{t('signUp')}</li>
                    </BreadCrumb>
                    <div className="floating-box pt-2 pb-2">

                        <h3 className="h3 text-center mt-3">{t('registering')}</h3>
                        <div className="col-12 text-center pt-2">
                            <div style={{color: "#7749F8", fontSize: 14, marginBottom: "1rem"}}>
                                {t('obligatoryFields')}
                            </div>
                        </div>

                        <Form className="row g-3">
                            <div className="overlay" style={{display: submitting ? 'inline-flex' : 'none'}}>
                                <div class="spinner-border" role="status">
                                    <span class="sr-only">Loading...</span>
                                </div>
                            </div>

                            <GridItemInput name="login" placeholder={t("login")} type="text"/>
                            <GridItemInput name="email" placeholder={t("emailAddress")} type="email"/>
                            <GridItemInput name="password" placeholder={t("password")} type="password"/>
                            <GridItemInput name="repeatedPassword" placeholder={t("repeatPassword")} type="password"/>
                            <GridItemInput name="firstname" placeholder={t("name")} type="text"/>
                            <GridItemInput name="lastname" placeholder={t("surname")} type="text"/>
                            <GridItemInput name="contactNumber" placeholder={t("phoneNumber")} type="text"/>

                            <div className="col-12 d-flex justify-content-center mb-2">
                                <ReCAPTCHA key={reCaptchaKey} theme={colorTheme} style={{display: "inline-block"}}
                                           hl={i18n.language} ref={recaptchaRef}
                                           sitekey={process.env.REACT_APP_RECAPTCHA_SITE_KEY}/>
                            </div>

                            <div className="col-12 d-flex justify-content-center mb-3">
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