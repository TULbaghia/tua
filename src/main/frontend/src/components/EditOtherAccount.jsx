import React, {useState} from "react";
import {useHistory} from "react-router";
import {useLocale} from "./LoginContext";
import { withNamespaces } from 'react-i18next';
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import { api } from "../Api";
import {
    useNotificationDangerAndLong,
    useNotificationSuccessAndShort,
    useNotificationWarningAndLong,
} from "./Notification/NotificationProvider";
import {useDialogPermanentChange} from "./CriticalOperations/CriticalOperationProvider";
import {Configuration, DefaultApi} from "api-client";

function EditOtherAccount(props) {
    const {t, i18n} = props
    const history = useHistory();
    const conf = new Configuration()
    const api = new DefaultApi(conf)
    const {token, setToken} = useLocale();
    const [etag, setETag] = useState();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [repeatedPassword, setRepeatedPassword] = useState();
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [contactNumber, setContactNumber] = useState('');
    const [clientCheckbox, setClientCheckbox] = useState('');
    const [managerCheckbox, setManagerCheckbox] = useState('');
    const [adminCheckbox, setAdminCheckbox] = useState('');
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationWarning = useNotificationWarningAndLong();
    const dispatchNotificationDanger = useNotificationDangerAndLong();
    const dispatchDialog = useDialogPermanentChange();

    React.useEffect(() => {
        if (token) {
            getEtag().then(r => setETag(r));
        }
    }, [token]);

    const getEtag = async () => {
        const response = await fetch("/resources/accounts/user", {
            method: "GET",
            headers: {
                Authorization: token,
            },
        });
        return response.headers.get("ETag");
    };

    const handleEmailSubmit = e => {
        e.preventDefault()
        if (!email.includes("@") || !email.includes(".")) {
            dispatchNotificationWarning({message: i18n.t('invalidEmailSyntax')})
        } else {
            dispatchDialog({
                callbackOnSave: () => {
                    editEmail()
                },
                callbackOnCancel: () => {
                    console.log("Cancel")
                },
            })
        }
    }

    const editEmail = () => {
        api.editOtherAccountEmail(login, {newEmail: email}, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            history.push("/home");
            dispatchNotificationSuccess({message: i18n.t('emailChangeSuccess')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        });
    };

    const handlePasswordSubmit = e => {
        e.preventDefault()
        if (newPassword !== repeatedPassword) {
            dispatchNotificationWarning({message: i18n.t('passwordsNotMatch')})
        } else if (newPassword === oldPassword) {
            dispatchNotificationWarning({message: i18n.t('oldPasswordEqualsNew')})
        } else {
            dispatchDialog({
                callbackOnSave: () => {
                    editPassword()
                },
                callbackOnCancel: () => {
                    console.log("Cancel")
                },
            })
        }
    }

    const editPassword = () => {
        api.changeOtherPassword({login: login, oldPassword: oldPassword, newPassword: newPassword}, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            history.push("/home");
            dispatchNotificationSuccess({message: i18n.t('passwordChangeSuccess')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        });
    };

    const handleDetailsSubmit = e => {
        e.preventDefault()
        if (firstname.length < 3 || firstname.length > 31) {
            dispatchNotificationWarning({message: i18n.t('nameSize')})
        } else if (lastname.length < 2 || lastname.length > 31) {
            dispatchNotificationWarning({message: i18n.t('surnameSize')})
        } else if (contactNumber.length < 9 || contactNumber.length > 15) {
            dispatchNotificationWarning({message: i18n.t('phoneSize')})
        } else {
            dispatchDialog({
                callbackOnSave: () => {
                    editDetails()
                },
                callbackOnCancel: () => {
                    console.log("Cancel")
                },
            })
        }
    }

    const editDetails = () => {
        api.editOtherAccountDetails(login, {firstname: firstname, lastname: lastname, contactNumber: contactNumber}, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            history.push("/home");
            dispatchNotificationSuccess({message: i18n.t('detailsChangeSuccess')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        });
    };

    const handleRolesSubmit = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                editRoles()
            },
            callbackOnCancel: () => {
                console.log("Cancel")
            },
        })

    }

    const editRoles = () => {
        // if (document.getElementById('clientCheckbox').checked) {
        //     if (clientCheckbox === "CLIENT") {
        //         api.grantAccessLevel(login, "CLIENT"), {
        //             headers: {
        //                 "Content-Type": "application/json",
        //                 Authorization: token,
        //                 "If-Match": etag
        //             }
        //         }.then((res) => {
        //             dispatchNotificationSuccess({message: i18n.t('roleGrant.success')})
        //         }).catch(err => {
        //             dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        //         });
        //     } else if (document.getElementById('clientCheckbox').checked == false) {
        //         if (clientCheckbox === "CLIENT") {
        //             api.revokeAccessLevel(login, "CLIENT"), {
        //                 headers: {
        //                     "Content-Type": "application/json",
        //                     Authorization: token,
        //                     "If-Match": etag
        //                 }
        //             }.then((res) => {
        //                 dispatchNotificationSuccess({message: i18n.t('roleRevoke.success')})
        //             }).catch(err => {
        //                 dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        //             });
        //         }
        //     }
        // }
        // if (document.getElementById('managerCheckbox').checked) {
        //     if (managerCheckbox === "MANAGER") {
        //         api.grantAccessLevel(login, "MANAGER"), {
        //             headers: {
        //                 "Content-Type": "application/json",
        //                 Authorization: token,
        //                 "If-Match": etag
        //             }
        //         }.then((res) => {
        //             dispatchNotificationSuccess({message: i18n.t('roleGrant.success')})
        //         }).catch(err => {
        //             dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        //         });
        //     }
        // } else if (document.getElementById('managerCheckbox').checked == false) {
        //     if (managerCheckbox === "MANAGER") {
        //         api.revokeAccessLevel(login, "MANAGER"), {
        //             headers: {
        //                 "Content-Type": "application/json",
        //                 Authorization: token,
        //                 "If-Match": etag
        //             }
        //         }.then((res) => {
        //             dispatchNotificationSuccess({message: i18n.t('roleRevoke.success')})
        //         }).catch(err => {
        //             dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        //         });
        //     }
        // }
        //
        // if (document.getElementById('adminCheckbox').checked) {
        //     if (adminCheckbox === "ADMIN") {
        //         api.grantAccessLevel(login, "ADMIN"), {
        //             headers: {
        //                 "Content-Type": "application/json",
        //                 Authorization: token,
        //                 "If-Match": etag
        //             }
        //         }.then((res) => {
        //             dispatchNotificationSuccess({message: i18n.t('roleGrant.success')})
        //         }).catch(err => {
        //             dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        //         });
        //     }
        // } else if (document.getElementById('adminCheckbox').checked == false) {
        //     if (adminCheckbox === "ADMIN") {
        //         api.revokeAccessLevel(login, "ADMIN"), {
        //             headers: {
        //                 "Content-Type": "application/json",
        //                 Authorization: token,
        //                 "If-Match": etag
        //             }
        //         }.then((res) => {
        //             dispatchNotificationSuccess({message: i18n.t('roleRevoke.success')})
        //         }).catch(err => {
        //             dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        //         });
        //     }
        // }
    }

    const onChangeClient = () => {
        setClientCheckbox("CLIENT")
    }

    const onChangeManager = () => {
        setManagerCheckbox("MANAGER")
    }

    const onChangeAdmin = () => {
        setAdminCheckbox("ADMIN")
    }

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item"><Link to="/">{t('adminDashboard')}</Link></li>
                <li className="breadcrumb-item"><Link to="/accounts">{t('accountList')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('userEdit')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <form className="form-signup">
                    <h1 className="h3">{t('userEdit')}</h1>
                    <div style={{color: "#7749F8", fontSize: 14, marginBottom: "1rem"}}>
                        {t('obligatoryFields')}
                    </div>
                    <input
                        id="inputName"
                        className="form-control"
                        placeholder={t('emailAddress')}
                        required
                        autoFocus={true}
                        onChange={event => setEmail(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <button className="btn btn-lg btn-primary btn-block" onClick={handleEmailSubmit}
                            type="submit"
                            style={{backgroundColor: "#7749F8"}}>
                        {i18n.t('changeEmail')}
                    </button>
                    <input
                        id="oldPassword"
                        className="form-control"
                        placeholder={i18n.t('oldPassword')}
                        required
                        autoFocus={true}
                        onChange={event => setOldPassword(event.target.value)}
                        style={{marginTop: "2rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        id="newPassword"
                        className="form-control"
                        placeholder={i18n.t('newPassword')}
                        required
                        autoFocus={true}
                        onChange={event => setNewPassword(event.target.value)}
                        style={{marginTop: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        id="repeatNewPassword"
                        className="form-control"
                        placeholder={i18n.t('repeatNewPassword')}
                        required
                        autoFocus={true}
                        onChange={event => setRepeatedPassword(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <button className="btn btn-lg btn-primary btn-block" onClick={handlePasswordSubmit}
                            type="submit"
                            style={{backgroundColor: "#7749F8"}}>
                        {i18n.t('changePassword')}
                    </button>
                    <input
                        id="inputName"
                        className="form-control"
                        placeholder={t('name')}
                        required
                        autoFocus={true}
                        onChange={event => setFirstname(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        id="inputSurname"
                        className="form-control"
                        placeholder={t('surname')}
                        required
                        autoFocus={true}
                        onChange={event => setLastname(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        id="inputPhoneNumber"
                        className="form-control"
                        placeholder={t('phoneNumber')}
                        required
                        autoFocus={true}
                        onChange={event => setContactNumber(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "0rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <div style={{marginBottom: "1rem"}}/>
                    <button className="btn btn-lg btn-primary btn-block" onClick={handleDetailsSubmit}
                            type="submit"
                            style={{backgroundColor: "#7749F8"}}>
                        {i18n.t('changeDetails')}
                    </button>
                    <div style={{marginBottom: "1rem"}}/>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="checkbox" id="clientCheckbox" value="CLIENT"
                               style={{backgroundColor: "#7749F8"}} onClick={onChangeClient}/>
                        <label className="form-check-label" htmlFor="inlineCheckbox1">{t('client')}</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="checkbox" id="managerCheckbox" value="MANAGER"
                               style={{backgroundColor: "#7749F8"}} onChange={onChangeManager}/>
                        <label className="form-check-label" htmlFor="inlineCheckbox2">{t('manager')}</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="checkbox" id="adminCheckbox" value="ADMIN"
                               style={{backgroundColor: "#7749F8"}} onChange={onChangeAdmin}/>
                        <label className="form-check-label" htmlFor="inlineCheckbox3">{t('admin')}</label>
                    </div>
                    <div style={{marginBottom: "1rem"}}/>
                    <button className="btn btn-lg btn-primary btn-block" type="submit"
                            style={{backgroundColor: "#7749F8"}} onClick={handleRolesSubmit}>
                        {t('changeRoles')}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default withNamespaces()(EditOtherAccount);