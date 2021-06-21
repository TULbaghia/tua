import React, {useState} from "react";
import {useHistory} from "react-router";
import {useLocale} from "../LoginContext";
import BreadCrumb from "../Partial/BreadCrumb";
import {withNamespaces} from "react-i18next";
import ReCAPTCHA from "react-google-recaptcha";
import {Link} from "react-router-dom";
import {Configuration, DefaultApi} from "ssbd06-api";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort,
    useNotificationWarningAndLong,
} from "../Utils/Notification/NotificationProvider";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import i18n from '../../i18n';
import {Form, Formik} from 'formik';
import {handleRecaptcha} from "../Recaptcha/RecaptchaCallback";
import FieldComponent from "../EditOwnAccount/FieldComponent";
import {validatorFactory, ValidatorType} from "../Validation/Validators";
import Tabs from "react-bootstrap/Tabs";
import Tab from "react-bootstrap/Tab";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {Col, Container, Row} from "react-bootstrap";

function EditOwnAccount() {
    const history = useHistory();
    const {token, setToken} = useLocale();
    const [etag, setETag] = useState();

    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationWarning = useNotificationWarningAndLong();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchDialog = useDialogPermanentChange();

    const conf = new Configuration();
    const api = new DefaultApi(conf);
    const recaptchaRef1 = React.createRef();
    const recaptchaRef2 = React.createRef();
    const recaptchaRef3 = React.createRef();

    React.useEffect(() => {
        if (token) {
            getEtag()
                .then(r => setETag(r))
                .catch((err) => ResponseErrorHandler(err, dispatchNotificationDanger));
        }
    }, [token]);

    const getEtag = async () => {
        const response = await api.showAccountInformation({
            method: "GET",
            headers: {
                Authorization: token,
            }
        })
        return response.headers.etag;
    };

    const handleEmailSubmit = (values, setSubmitting) => {
        handleRecaptcha(() => {
            dispatchDialog({
                callbackOnSave: () => {
                    editEmail(values, setSubmitting)
                },
                callbackOnCancel: () => {
                    setSubmitting(false)
                },
            })
        }, recaptchaRef1, dispatchNotificationWarning, () => setSubmitting(false))
    }

    const editEmail = (values, setSubmitting) => {
        api.editOwnAccountEmail({newEmail: values.email}, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            history.push("/myAccount");
            dispatchNotificationSuccess({message: i18n.t('emailChangeSuccess')})
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
        });
        setSubmitting(false);
    };

    const handlePasswordSubmit = (values, setSubmitting) => {
        handleRecaptcha(() => {
            dispatchDialog({
                callbackOnSave: () => {
                    editPassword(values, setSubmitting)
                },
                callbackOnCancel: () => {
                    setSubmitting(false)
                },
            })
        }, recaptchaRef2, dispatchNotificationWarning, () => setSubmitting(false))
    }


    const editPassword = (values, setSubmitting) => {
        api.changePassword({oldPassword: values.oldPassword, newPassword: values.newPassword}, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            history.push("/myAccount");
            dispatchNotificationSuccess({message: i18n.t('passwordChangeSuccess')})
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
        });
        setSubmitting(false)
    };

    const handleDetailsSubmit = (values, setSubmitting) => {
        handleRecaptcha(() => {
            dispatchDialog({
                callbackOnSave: () => {
                    editDetails(values, setSubmitting)
                },
                callbackOnCancel: () => {
                    setSubmitting(false)
                },
            })
        }, recaptchaRef3, dispatchNotificationWarning, () => setSubmitting(false))
    }

    const editDetails = (values, setSubmitting) => {
        api.editOwnAccountDetails({
            firstname: values.name,
            lastname: values.surname,
            contactNumber: values.phoneNumber
        }, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            history.push("/myAccount");
            dispatchNotificationSuccess({message: i18n.t('detailsChangeSuccess')})
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
        });
        setSubmitting(false)
    };

    return (
        <div className={"container-fluid"}>
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{i18n.t('mainPage')}</Link></li>
                <li className="breadcrumb-item"><Link to="/myAccount">{i18n.t('accountInfo')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{i18n.t('editAccount.title')}</li>
            </BreadCrumb>

            <Container>
                <Row>
                    <Col xs={12} sm={12} md={10} lg={9} xl={8} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div className="" style={{minHeight: "30rem"}}>
                            <div>
                                <h1 className="h3 mb-4">{i18n.t('editProfile')}</h1>
                                <div style={{color: "#7749F8", fontSize: 14, marginBottom: "0.5rem"}}>
                                    {i18n.t('obligatoryFields')}
                                </div>
                                <div className="container">
                                    <Tabs defaultActiveKey="tab1" className="categories m-3">
                                        <Tab eventKey="tab1" title={i18n.t('editEmail')}>
                                            <Formik key={i18n.language}
                                                    initialValues={{email: ''}}
                                                    validate={values => {
                                                        const errors = {};

                                                        if (!values.email) {
                                                            errors.email = i18n.t('email.error.required');
                                                        } else {
                                                            validatorFactory(values.email, ValidatorType.USER_EMAIL).forEach(x => {
                                                                errors.email = x;
                                                            })
                                                        }

                                                        return errors;
                                                    }}
                                                    onSubmit={(values, {setSubmitting}) => handleEmailSubmit(values, setSubmitting)}>

                                                {({isSubmitting, handleChange}) => (
                                                    <Form className={{alignItems: "center"}}>
                                                        <FieldComponent type="text" name="email"
                                                                        placeholder={i18n.t('emailAddress')}
                                                                        handleChange={handleChange}/>
                                                        <div className={"d-flex w-100 justify-content-center py-2"}>
                                                            <ReCAPTCHA key={i18n.language} hl={i18n.language}
                                                                       ref={recaptchaRef1}
                                                                       sitekey={process.env.REACT_APP_RECAPTCHA_SITE_KEY}/>
                                                        </div>
                                                        <button className="btn btn-lg btn-primary btn-block mb-3"
                                                                type="submit" disabled={isSubmitting}
                                                                style={{backgroundColor: "#7749F8"}}>
                                                            {i18n.t('changeEmail')}
                                                        </button>
                                                    </Form>
                                                )}
                                            </Formik>
                                        </Tab>
                                        <Tab eventKey="tab2" title={i18n.t('editPassword')}>
                                            <Formik
                                                initialValues={{
                                                    oldPassword: '',
                                                    newPassword: '',
                                                    repeatNewPassword: ''
                                                }}
                                                validate={values => {
                                                    const errors = {};

                                                    if (!values.oldPassword) {
                                                        errors.oldPassword = i18n.t('oldPassword.error.required');
                                                    } else {
                                                        validatorFactory(values.oldPassword, ValidatorType.PASSWORD).forEach(x => {
                                                            errors.oldPassword = x;
                                                        })
                                                    }

                                                    if (!values.newPassword) {
                                                        errors.newPassword = i18n.t('passwordResetForm.error.required');
                                                    } else {
                                                        validatorFactory(values.newPassword, ValidatorType.PASSWORD).forEach(x => {
                                                            errors.newPassword = x;
                                                        })
                                                    }

                                                    if (!values.repeatedNewPassword) {
                                                        errors.repeatedNewPassword = i18n.t('passwordResetForm.error.repeated.required');
                                                    } else {
                                                        validatorFactory(values.repeatedNewPassword, ValidatorType.PASSWORD).forEach(x => {
                                                            errors.repeatedNewPassword = x;
                                                        })
                                                    }

                                                    if (!(values.repeatedNewPassword === values.newPassword)) {
                                                        errors.repeatedNewPassword = i18n.t('passwordResetForm.error.match');
                                                    }

                                                    if (values.oldPassword === values.newPassword) {
                                                        errors.repeatedNewPassword = i18n.t('passwordResetForm.error.oldPasswordEqualsNew');
                                                    }

                                                    return errors;
                                                }}
                                                onSubmit={(values, {setSubmitting}) => handlePasswordSubmit(values, setSubmitting)}>


                                                {({isSubmitting, handleChange}) => (
                                                    <Form className={{alignItems: "center"}}>

                                                        <FieldComponent type="password" name="oldPassword"
                                                                        placeholder={i18n.t('oldPassword')}
                                                                        handleChange={handleChange}/>
                                                        <FieldComponent type="password" name="newPassword"
                                                                        placeholder={i18n.t('newPassword')}
                                                                        handleChange={handleChange}/>
                                                        <FieldComponent type="password" name="repeatedNewPassword"
                                                                        placeholder={i18n.t('repeatPassword')}
                                                                        handleChange={handleChange}/>
                                                        <div className={"d-flex w-100 justify-content-center py-2"}>
                                                            <ReCAPTCHA key={i18n.language} hl={i18n.language}
                                                                       ref={recaptchaRef2}
                                                                       sitekey={process.env.REACT_APP_RECAPTCHA_SITE_KEY}/>
                                                        </div>
                                                        <button className="btn btn-lg btn-primary btn-block mb-3"
                                                                type="submit" disabled={isSubmitting}
                                                                style={{backgroundColor: "#7749F8"}}>
                                                            {i18n.t('changePassword')}
                                                        </button>
                                                    </Form>
                                                )}
                                            </Formik>
                                        </Tab>
                                        <Tab eventKey="tab3" title={i18n.t('editDetails')}>
                                            <Formik
                                                initialValues={{
                                                    name: '',
                                                    surname: '',
                                                    phoneNumber: ''
                                                }}
                                                validate={values => {
                                                    const errors = {};

                                                    if (!values.name) {
                                                        errors.name = i18n.t('name.error.required');
                                                    } else {
                                                        validatorFactory(values.name, ValidatorType.FIRSTNAME).forEach(x => {
                                                            errors.name = x;
                                                        })
                                                    }

                                                    if (!values.surname) {
                                                        errors.surname = i18n.t('surname.error.required');
                                                    } else {
                                                        validatorFactory(values.surname, ValidatorType.LASTNAME).forEach(x => {
                                                            errors.surname = x;
                                                        })
                                                    }

                                                    if (!values.phoneNumber) {
                                                        errors.phoneNumber = i18n.t('contactNumber.error.required');
                                                    } else {
                                                        validatorFactory(values.phoneNumber, ValidatorType.CONTACT_NUMBER).forEach(x => {
                                                            errors.phoneNumber = x;
                                                        })
                                                    }

                                                    return errors;
                                                }}
                                                onSubmit={(values, {setSubmitting}) => handleDetailsSubmit(values, setSubmitting)}>

                                                {({isSubmitting, handleChange}) => (
                                                    <Form className={{alignItems: "center"}}>

                                                        <FieldComponent type="text" name="name"
                                                                        placeholder={i18n.t('name')}
                                                                        handleChange={handleChange}/>
                                                        <FieldComponent type="text" name="surname"
                                                                        placeholder={i18n.t('surname')}
                                                                        handleChange={handleChange}/>
                                                        <FieldComponent type="text" name="phoneNumber"
                                                                        placeholder={i18n.t('phoneNumber')}
                                                                        handleChange={handleChange}/>
                                                        <div className={"d-flex w-100 justify-content-center py-2"}>
                                                            <ReCAPTCHA key={i18n.language} hl={i18n.language}
                                                                       ref={recaptchaRef3}
                                                                       sitekey={process.env.REACT_APP_RECAPTCHA_SITE_KEY}/>
                                                        </div>
                                                        <button className="btn btn-lg btn-primary btn-block mb-3"
                                                                type="submit" disabled={isSubmitting}
                                                                style={{backgroundColor: "#7749F8"}}>
                                                            {i18n.t('changeDetails')}
                                                        </button>
                                                    </Form>
                                                )}
                                            </Formik>
                                        </Tab>
                                    </Tabs>
                                </div>
                            </div>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(EditOwnAccount);