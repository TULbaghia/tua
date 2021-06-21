import React, {useState} from "react";
import {useHistory, useLocation} from "react-router";
import {withNamespaces} from "react-i18next";
import {Configuration, DefaultApi} from "ssbd06-api";
import {Link, useParams} from "react-router-dom";
import {Form, Formik} from 'formik';
import PasswordComponent from "./PasswordComponent";
import BreadCrumb from "../Partial/BreadCrumb";
import {
    useNotificationCustom,
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import EmailComponent from "./EmailComponent";
import FirstnameComponent from "./FirstnameComponent";
import LastnameComponent from "./LastnameComponent";
import ContactNumberComponent from "./ContactNumberComponent";
import {useLocale} from "../LoginContext";
import Tab from "react-bootstrap/Tab";
import Tabs from "react-bootstrap/Tabs";
import {Button} from "react-bootstrap";
import {rolesConstant} from "../../Constants";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import queryString from "query-string";

function BookingForm({t, i18n}) {
    const dispatchNotification = useNotificationCustom();
    const dispatchCriticalDialog = useDialogPermanentChange();
    const history = useHistory();
    let {code} = useParams();
    const conf = new Configuration()
    const api = new DefaultApi(conf)
    const {token, setToken} = useLocale();
    const [etag, setETag] = useState();
    const [etagRole, setETagRole] = useState();
    const location = useLocation();
    const parsedQuery = queryString.parse(location.search);
    const [roles, setRoles] = useState("");
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchDialog = useDialogPermanentChange();

    const getRoles = async () => {
        return await api.getUserRole(parsedQuery.login, {headers: {Authorization: token}});
    }

    const isRole = (role) => {
        return roles.includes(role);
    }

    React.useEffect(() => {
        handleDataFetch();
    }, []);

    const handleDataFetch = (firstTime = true) => {
        let etagsGet = false
        if (token) {
            getEtag(parsedQuery.login).then(r => {setETag(r); etagsGet = true;});
            getEtagRole(parsedQuery.login).then(r => {setETagRole(r); if (etagsGet) {etagsGet = true;}});
            getRoles().then(res => {
                console.log(res.data);
                let data = "";
                let i;
                for (i = 0; i < res.data.rolesGranted.length; i++) {
                    data += res.data.rolesGranted[i].roleName + ", ";
                }
                data = data.slice(0, data.length - 2)
                setRoles(data);
                if (etagsGet && !firstTime) {
                    dispatchNotificationSuccess({message: i18n.t('dataRefresh')})
                }
            }).catch(err => {
                if (err.response != null) {
                    if (err.response.status === 403) {
                        history.push("/errors/forbidden");
                    } else if (err.response.status === 500) {
                        history.push("/errors/internal");
                    }
                }
            });
        }
    }

    const getEtag = async (login) => {
        const response = await api.showAccount(login,{
            method: "GET",
            headers: {
                Authorization: token,
            }})
        return response.headers.etag;
    };

    const getEtagRole = async (login) => {
        const response = await api.getUserRole(login,{
            method: "GET",
            headers: {
                Authorization: token,
            }})
        return response.headers.etag;
    };

    const addRole = (role) => (
        api.grantAccessLevel(parsedQuery.login, role, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etagRole
            }
        }).then(res => {
            dispatchNotificationSuccess({message: i18n.t('roleGrant.success')})
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
        })
    )

    const revokeRole = (role) => (
        api.revokeAccessLevel(parsedQuery.login, role, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etagRole
            }
        }).then((res) => {
            dispatchNotificationSuccess({message: i18n.t('roleRevoke.success')})
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
        })
    )

    const handleAddRoleClient = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                addRole(rolesConstant.client)
            },
        })
    }

    const handleAddRoleManager = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                addRole(rolesConstant.manager)
            },

        })
    }

    const handleAddRoleAdmin = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                addRole(rolesConstant.admin)
            },

        })
    }

    const handleRevokeRoleClient = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                revokeRole(rolesConstant.client)
            },

        })
    }

    const handleRevokeRoleManager = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                revokeRole(rolesConstant.manager)
            },

        })
    }

    const handleRevokeRoleAdmin = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                revokeRole(rolesConstant.admin)
            },
        })
    }

    const handleEmailConfirmation = (values, setSubmitting) => (
        dispatchCriticalDialog({
            callbackOnSave: () => handleEmailSubmit(values, setSubmitting),
            callbackOnCancel: () => setSubmitting(false)
        })
    )

    const handlePasswordConfirmation = (values, setSubmitting) => (
        dispatchCriticalDialog({
            callbackOnSave: () => handlePasswordSubmit(values, setSubmitting),
            callbackOnCancel: () => setSubmitting(false)
        })
    )

    const handleDetailsConfirmation = (values, setSubmitting) => (
        dispatchCriticalDialog({
            callbackOnSave: () => handleDetailsSubmit(values, setSubmitting),
            callbackOnCancel: () => setSubmitting(false)
        })
    )

    const handleEmailSubmit = (values, setSubmitting) => {
        api.editOtherAccountEmail(parsedQuery.login, {newEmail: values.email}, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            dispatchNotificationSuccess({message: t('editOtherAccount.success.email')});
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
            setSubmitting(false);
        });
    }

    const handlePasswordSubmit = (values, setSubmitting) => {
        api.changeOtherPassword({login: parsedQuery.login, givenPassword: values.newPassword}, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            dispatchNotificationSuccess({message: t('editOtherAccount.success.password')});
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
            setSubmitting(false);
        });
    }

    const handleDetailsSubmit = (values, setSubmitting) => {
        api.editOtherAccountDetails(parsedQuery.login, {
            firstname: values.firstname,
            lastname: values.lastname,
            contactNumber: values.contactNumber
        }, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            dispatchNotificationSuccess({message: t('editOtherAccount.success.details')});
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
            setSubmitting(false);
        });
    }

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item"><Link to="/userPage">{t('adminDashboard')}</Link></li>
                <li className="breadcrumb-item"><Link to="/accounts">{t('accountList')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('userEdit')}</li>
            </BreadCrumb>

            <div className="floating-box" style={{minWidth: "30rem", minHeight: "30rem"}}>
                <div>
                    <div className={"d-flex text-center flex-column"}>
                        <h3 className="h3 mb-0.5">{t('userEdit')}: {parsedQuery.login}</h3>
                        <Button className="btn btn-secondary my-2"
                                style={{backgroundColor: "#7749F8", width: "20%", margin: "auto"}} onClick={event => {
                            handleDataFetch(false)
                        }}>{t("refresh")}</Button>
                        <div style={{color: "#7749F8", fontSize: 14, marginBottom: "0.5rem"}}>
                            {t('obligatoryFields')}
                        </div>
                    </div>
                    <div className="container">
                        <Tabs defaultActiveKey="tab1" className="categories my-3">
                            <Tab eventKey="tab1" title={t('editEmail')}>
                                <Formik
                                    initialValues={{email: ''}}
                                    validate={values => {
                                        const errors = {};
                                        let mailPattern = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                                        if (!values.email) {
                                            errors.email = t('editOtherAccountForm.error.email.required');
                                        } else if (values.email.length > 127 || values.email.length < 6) {
                                            errors.email = t('editOtherAccountForm.error.email.length');
                                        } else if (!mailPattern.test(values.email)) {
                                            errors.email = t('editOtherAccountForm.error.email.pattern');
                                        }
                                        return errors;
                                    }}
                                    onSubmit={(values, {setSubmitting}) => handleEmailConfirmation(values, setSubmitting)}>
                                    {({handleChange}) => (
                                        <Form className={{alignItems: "center"}}>
                                            <EmailComponent name="email" placeholder={t('emailAddress')}
                                                            handleChange={handleChange}/>
                                            <button className="btn btn-lg btn-primary btn-block mt-2"
                                                    type="submit"
                                                    style={{backgroundColor: "#7749F8", width: "70%", margin: "auto"}}>
                                                {t('changeEmail')}
                                            </button>
                                        </Form>
                                    )}
                                </Formik>
                            </Tab>
                            <Tab eventKey="tab2" title={t('editPassword')}>
                                <Formik
                                    initialValues={{newPassword: '', repeatedNewPassword: ''}}
                                    validate={values => {
                                        const errors = {};
                                        if (!values.newPassword) {
                                            errors.newPassword = t('editOtherAccountForm.password.error.required');
                                        } else if (values.newPassword.length > 64 || values.newPassword.length < 8) {
                                            errors.newPassword = t('editOtherAccountForm.password.error.length');
                                        }

                                        if (!values.repeatedNewPassword) {
                                            errors.repeatedNewPassword = t('editOtherAccountForm.password.error.repeated.required');
                                        } else if (values.repeatedNewPassword.length > 64 || values.repeatedNewPassword.length < 8) {
                                            errors.repeatedNewPassword = t('editOtherAccountForm.password.error.length');
                                        }

                                        if (!(values.repeatedNewPassword === values.newPassword)) {
                                            errors.repeatedNewPassword = t('editOtherAccountForm.password.error.match');
                                        }
                                        return errors;
                                    }}
                                    onSubmit={(values, {setSubmitting}) => handlePasswordConfirmation(values, setSubmitting)}>
                                    {({handleChange}) => (
                                        <Form className={{alignItems: "center"}}>
                                            <PasswordComponent name="newPassword" placeholder={t('password')}
                                                               handleChange={handleChange}/>
                                            <PasswordComponent name="repeatedNewPassword" placeholder={t('repeatPassword')}
                                                               handleChange={handleChange}/>
                                            <button className="btn btn-lg btn-primary btn-block mt-2"
                                                    type="submit"
                                                    style={{backgroundColor: "#7749F8", width: "70%", margin: "auto"}}>
                                                {t('changePassword')}
                                            </button>
                                        </Form>
                                    )}
                                </Formik>
                            </Tab>
                            <Tab eventKey="tab3" title={t('editDetails')}>
                                <Formik
                                    initialValues={{firstname: '', lastname: '', contactNumber: ''}}
                                    validate={values => {
                                        const errors = {};
                                        let firstnamePattern = /^[A-ZĆŁÓŚŹŻ\s]{1}[a-ząęćńóśłźż]+$/;
                                        let lastnamePattern = /^[A-ZĆŁÓŚŹŻ\s]{1}[a-ząęćńóśłźż]+$/;
                                        let contactNumberPattern = /^[0-9\+][0-9]{8,14}$/;
                                        if (!values.firstname) {
                                            errors.firstname = t('editOtherAccountForm.firstname.error.required');
                                        } else if (values.firstname.length > 31 || values.firstname.length < 3) {
                                            errors.firstname = t('editOtherAccountForm.firstname.error.length');
                                        } else if (!firstnamePattern.test(values.firstname)) {
                                            errors.firstname = t('editOtherAccountForm.firstname.error.pattern');
                                        }

                                        if (!values.lastname) {
                                            errors.lastname = t('editOtherAccountForm.lastname.error.required');
                                        } else if (values.lastname.length > 31 || values.lastname.length < 2) {
                                            errors.lastname = t('editOtherAccountForm.lastname.error.length');
                                        } else if (!lastnamePattern.test(values.lastname)) {
                                            errors.lastname = t('editOtherAccountForm.lastname.error.pattern');
                                        }

                                        if (!values.contactNumber) {
                                            errors.contactNumber = t('editOtherAccountForm.contactNumber.error.required');
                                        } else if (values.contactNumber.length > 15 || values.contactNumber.length < 9) {
                                            errors.contactNumber = t('editOtherAccountForm.contactNumber.error.length');
                                        } else if (!contactNumberPattern.test(values.contactNumber)) {
                                            errors.contactNumber = t('editOtherAccountForm.contactNumber.error.pattern');
                                        }
                                        return errors;
                                    }}
                                    onSubmit={(values, {setSubmitting}) => handleDetailsConfirmation(values, setSubmitting)}>
                                    {({handleChange}) => (
                                        <Form className={{alignItems: "center"}}>
                                            <FirstnameComponent name="firstname" placeholder={t('name')}
                                                                handleChange={handleChange}/>
                                            <LastnameComponent name="lastname" placeholder={t('surname')}
                                                               handleChange={handleChange}/>
                                            <ContactNumberComponent name="contactNumber" placeholder={t('phoneNumber')}
                                                                    handleChange={handleChange}/>
                                            <button className="btn btn-lg btn-primary btn-block mt-2"
                                                    type="submit"
                                                    style={{backgroundColor: "#7749F8", width: "70%", margin: "auto"}}>
                                                {t('changeDetails')}
                                            </button>
                                        </Form>
                                    )}
                                </Formik>
                            </Tab>
                            <Tab eventKey="tab4" title={t('editRoles')}>
                                {!isRole(rolesConstant.client) &&
                                <button className="btn btn-lg btn-primary btn-block" type="submit"
                                        style={{backgroundColor: "forestgreen", marginBottom: "1rem"}}
                                        onClick={handleAddRoleClient}>
                                    {t('addRoleClient')}
                                </button>
                                }
                                {isRole(rolesConstant.client) &&
                                <button className="btn btn-lg btn-primary btn-block" type="submit"
                                        style={{backgroundColor: "#dc3545", marginBottom: "1rem"}}
                                        onClick={handleRevokeRoleClient}>
                                    {t('revokeRoleClient')}
                                </button>
                                }
                                {!isRole(rolesConstant.manager) &&
                                <button className="btn btn-lg btn-primary btn-block" type="submit"
                                        style={{backgroundColor: "forestgreen", marginBottom: "1rem"}}
                                        onClick={handleAddRoleManager}>
                                    {t('addRoleManager')}
                                </button>
                                }
                                {isRole(rolesConstant.manager) &&
                                <button className="btn btn-lg btn-primary btn-block" type="submit"
                                        style={{backgroundColor: "#dc3545", marginBottom: "1rem"}}
                                        onClick={handleRevokeRoleManager}>
                                    {t('revokeRoleManager')}
                                </button>
                                }
                                {!isRole(rolesConstant.admin) &&
                                <button className="btn btn-lg btn-primary btn-block" type="submit"
                                        style={{backgroundColor: "forestgreen", marginBottom: "1rem"}}
                                        onClick={handleAddRoleAdmin}>
                                    {t('addRoleAdmin')}
                                </button>
                                }
                                {isRole(rolesConstant.admin) &&
                                <button className="btn btn-lg btn-primary btn-block" type="submit"
                                        style={{backgroundColor: "#dc3545", marginBottom: "1rem"}}
                                        onClick={handleRevokeRoleAdmin}>
                                    {t('revokeRoleAdmin')}
                                </button>
                                }
                            </Tab>
                        </Tabs>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default withNamespaces()(BookingForm);
