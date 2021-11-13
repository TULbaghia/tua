import React from "react";
import {useHistory} from "react-router";
import {useLocale} from "../LoginContext";
import {withNamespaces} from 'react-i18next';
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {buildApi} from "../../Api";
import {Form, Formik} from 'formik';
import "../../css/Login.css"
import {validatorFactory, ValidatorType} from "../Validation/Validators";
import {
    useNotificationCustom,
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {dialogDuration, dialogType} from "../Utils/Notification/Notification";
import axios from "axios";
import jwt_decode from "jwt-decode";
import LoginFieldComponent from "./LoginFieldComponent";
import FieldComponent from "../PasswordReset/FieldComponent";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {Col, Container, Row} from "react-bootstrap";

const REFRESH_TIME = 60 * 1000;

function Login(props) {
    const {t, i18n} = props
    const history = useHistory();
    const {token, setToken, saveToken, setUsername, setCurrentRole} = useLocale();
    const dispatchDangerNotification = useNotificationDangerAndInfinity();

    const dispatch = useNotificationCustom();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();

    const handleRefreshBox = () => {
        dispatch({
            "type": dialogType.WARNING,
            "duration": dialogDuration.MINUTE,
            "title": `${i18n.t('dialog.expiring_token.title')}`,
            "message":
                (<> {i18n.t('dialog.expiring_token.message')} <span className={"text-primary"}
                                                                    style={{cursor: "pointer"}}
                                                                    onClick={refreshToken}>{i18n.t('dialog.expiring_token.refresh')}</span></>)
        })
    }

    const refreshToken = (event) => {
        event.target.closest(".alert").querySelector(".close").click();
        if (localStorage.getItem("token")) {
            axios.post(`${document.location.origin}/resources/auth/refresh-token`, localStorage.getItem("token"), {
                headers: {
                    "Authorization": `${localStorage.getItem("token")}`
                }
            }).then(response => {
                saveToken("Bearer " + response.data);
                clearTimeout(localStorage.getItem("timeoutId2") ?? 0)
                clearTimeout(localStorage.getItem("timeoutId3") ?? 0)
                schedule()
                dispatchNotificationSuccess({message: i18n.t('tokenRefreshSuccess')})
            }).catch(
                e => ResponseErrorHandler(e, dispatchDangerNotification)
            );
        }
    }

    const handleSubmit = async (values, setSubmitting) => {
        try {
            const api = buildApi(i18n.language);
            const res = await api.login({
                login: values.login,
                password: values.password
            })
            saveToken("Bearer " + res.data)
            history.push("/userPage")
            schedule();
        } catch (ex) {
            ResponseErrorHandler(ex, dispatchDangerNotification, true, (e) => {
            }, true)
            setSubmitting(false);
        }
    }

    const schedule = () => {
        const id = setTimeout(() => {
            handleRefreshBox();
        }, new Date(jwt_decode(localStorage.getItem("token")).exp * 1000) - new Date() - REFRESH_TIME);
        localStorage.setItem("timeoutId", id.toString())

        const id2 = setTimeout(() => {
            localStorage.removeItem("token")
            localStorage.removeItem("username")
            localStorage.removeItem("currentRole")
            setToken(null)
            setUsername('')
            setCurrentRole('')
            history.push("/")
        }, new Date(jwt_decode(localStorage.getItem("token")).exp * 1000) - new Date() + 2000);
        localStorage.setItem("timeoutId2", id2.toString())
    }

    return (
        <div className="mb-2 container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('logging')}</li>
            </BreadCrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={8} md={7} lg={6} xl={5} className={"floating-no-absolute py-4 mx-auto mb-2 px-4"}>
                        <div className="">
                            <h1 className="h3">{t('logging')}</h1>
                            <Formik initialValues={{login: '', password: ''}}
                                    validate={values => {
                                        const errors = {};

                                        if (!values.login) {
                                            errors.login = t('LoginForm.error.login.required');
                                        } else {
                                            validatorFactory(values.login, ValidatorType.LOGIN).forEach(x => {
                                                errors.login = x;
                                            })
                                        }

                                        if (!values.password) {
                                            errors.password = t('LoginForm.error.password.required');
                                        } else {
                                            validatorFactory(values.password, ValidatorType.PASSWORD).forEach(x => {
                                                errors.password = x;
                                            })
                                        }

                                        return errors;
                                    }}
                                    onSubmit={(values, {setSubmitting}) => {
                                        handleSubmit(values, setSubmitting);
                                    }}>

                                {({isSubmitting, handleChange}) => (
                                    <Form id={"loginFormPage"} className={"align-items-center"}>
                                        <LoginFieldComponent name="login" placeholder={t('login')}
                                                             handleChange={handleChange}/>
                                        <FieldComponent name="password" placeholder={t('password')}
                                                        handleChange={handleChange}/>
                                        <button className="btn btn-lg btn-purple btn-block mt-2"
                                                type="submit" disabled={isSubmitting}
                                                style={{margin: "auto"}}>
                                            {t('signIn')}
                                        </button>
                                        <button className="btn btn-lg btn-purple btn-block mt-2" type="button"
                                                onClick={() => history.push("/login/password-reset")}
                                                style={{margin: "auto"}}>
                                            {t('passwordReset')}
                                        </button>
                                    </Form>
                                )}
                            </Formik>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(Login);