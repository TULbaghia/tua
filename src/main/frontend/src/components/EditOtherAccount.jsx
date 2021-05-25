import React, {useState} from "react";
import {useHistory, useLocation} from "react-router";
import {useLocale} from "./LoginContext";
import { withNamespaces } from 'react-i18next';
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
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
    const [etagRole, setETagRole] = useState();
    const [login, setLogin] = useState('');
    const [email, setEmail] = useState('');
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [repeatedPassword, setRepeatedPassword] = useState();
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [contactNumber, setContactNumber] = useState('');
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationWarning = useNotificationWarningAndLong();
    const dispatchNotificationDanger = useNotificationDangerAndLong();
    const dispatchDialog = useDialogPermanentChange();
    const [roles, setRoles] = useState("");
    const location = useLocation();

    const getRoles = async () => {
        return await api.getUserRole(location.login, {headers: {Authorization: token}});
    }

    const isClient = () => {
        return roles.includes("CLIENT");
    }

    const isManager = () => {
        return roles.includes("MANAGER");
    }

    const isAdmin = () => {
        return roles.includes("ADMIN");
    }

    React.useEffect(() => {
        handleDataFetch();
    }, []);

    const handleDataFetch = () => {
        if (token) {
            getEtag().then(r => setETag(r));
            getEtagRole(location.login).then(r => setETagRole(r));
            getRoles().then(res => {
                let data = "";
                let i;
                for(i = 0; i < res.data.rolesGranted.length; i++) {
                    data += res.data.rolesGranted[i].roleName + ", ";
                }
                data = data.slice(0, data.length-2)
                setRoles(data);
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

    const getEtag = async () => {
        const response = await fetch("/resources/accounts/user", {
            method: "GET",
            headers: {
                Authorization: token,
            },
        });
        return response.headers.get("ETag");
    };

    const getEtagRole = async (login) => {
        const response = await fetch("/" + login + "/role", {
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
        api.editOtherAccountEmail(location.login, {newEmail: email}, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            history.push("/myAccount");
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
        api.changeOtherPassword({login: location.login, oldPassword: oldPassword, newPassword: newPassword}, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            history.push("/myAccount");
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
        api.editOtherAccountDetails(location.login, {firstname: firstname, lastname: lastname, contactNumber: contactNumber}, {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            history.push("/myAccount");
            dispatchNotificationSuccess({message: i18n.t('detailsChangeSuccess')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        });
    };

    const handleAddRoleClient = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                addRoleClient()
            },
            callbackOnCancel: () => {
                console.log("Cancel")
            },
        })
    }

    const handleAddRoleManager = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                addRoleManager()
            },
            callbackOnCancel: () => {
                console.log("Cancel")
            },
        })
    }

    const handleAddRoleAdmin = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                addRoleAdmin()
            },
            callbackOnCancel: () => {
                console.log("Cancel")
            },
        })
    }

    const handleRevokeRoleClient = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                revokeRoleClient()
            },
            callbackOnCancel: () => {
                console.log("Cancel")
            },
        })
    }

    const handleRevokeRoleManager = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                revokeRoleManager()
            },
            callbackOnCancel: () => {
                console.log("Cancel")
            },
        })
    }

    const handleRevokeRoleAdmin = e => {
        e.preventDefault()
        dispatchDialog({
            callbackOnSave: () => {
                revokeRoleAdmin()
            },
            callbackOnCancel: () => {
                console.log("Cancel")
            },
        })
    }

    const addRoleClient = () => (
        api.grantAccessLevel(location.login, "CLIENT", {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etagRole
            }
        }).then(res => {
            dispatchNotificationSuccess({message: i18n.t('roleGrant.success')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        })
    )

    const addRoleManager = () => (
        api.grantAccessLevel(location.login, "MANAGER", {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etagRole
            }
        }).then((res) => {
            dispatchNotificationSuccess({message: i18n.t('roleGrant.success')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        })
    )

    const addRoleAdmin = () => (
        api.grantAccessLevel(location.login, "ADMIN", {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etagRole
            }
        }).then((res) => {
            dispatchNotificationSuccess({message: i18n.t('roleGrant.success')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        })
    )

    const revokeRoleClient = () => (
        api.revokeAccessLevel(location.login, "CLIENT", {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etagRole
            }
        }).then((res) => {
            dispatchNotificationSuccess({message: i18n.t('roleRevoke.success')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        })
    )

    const revokeRoleManager = () => (
        api.revokeAccessLevel(location.login, "MANAGER", {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etagRole
            }
        }).then((res) => {
            dispatchNotificationSuccess({message: i18n.t('roleRevoke.success')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        })
    )

    const revokeRoleAdmin = () => (
        api.revokeAccessLevel(location.login, "ADMIN", {
            headers: {
                "Content-Type": "application/json",
                Authorization: token,
                "If-Match": etagRole
            }
        }).then((res) => {
            dispatchNotificationSuccess({message: i18n.t('roleRevoke.success')})
        }).catch(err => {
            dispatchNotificationDanger({message: i18n.t(err.response.data.message)})
        })
    )

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
                        type="password"
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
                        type="password"
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
                        type="password"
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
                    {isClient() &&
                        <button className="btn btn-lg btn-primary btn-block" type="submit"
                                style={{backgroundColor: "red", marginBottom: "1rem"}}
                                onClick={handleRevokeRoleClient}>
                            {t('revokeRoleClient')}
                        </button>
                    }
                    {isManager() &&
                        <button className="btn btn-lg btn-primary btn-block" type="submit"
                                style={{backgroundColor: "red", marginBottom: "1rem"}}
                                onClick={handleRevokeRoleManager}>
                            {t('revokeRoleManager')}
                        </button>
                    }
                    {isAdmin() &&
                        <button className="btn btn-lg  btn-primary btn-block" type="submit"
                                style={{backgroundColor: "red", marginBottom: "1rem"}}
                                onClick={handleRevokeRoleAdmin}>
                            {t('revokeRoleAdmin')}
                        </button>
                    }
                    {!isClient() &&
                        <button className="btn btn-lg btn-primary btn-block" type="submit"
                                style={{backgroundColor: "green", marginBottom: "1rem"}}
                                onClick={handleAddRoleClient}>
                            {t('addRoleClient')}
                        </button>
                    }
                    {!isManager() &&
                        <button className="btn btn-lg btn-primary btn-block" type="submit"
                                style={{backgroundColor: "green", marginBottom: "1rem"}}
                                onClick={handleAddRoleManager}>
                            {t('addRoleManager')}
                        </button>
                    }
                    {!isAdmin() &&
                        <button className="btn btn-lg btn-primary btn-block" type="submit"
                                style={{backgroundColor: "green", marginBottom: "1rem"}}
                                onClick={handleAddRoleAdmin}>
                            {t('addRoleAdmin')}
                        </button>
                    }
                    <button className="btn btn-lg btn-primary btn-block" style={{backgroundColor: "#7749F8", marginBottom: "1rem"}} onClick={event => {handleDataFetch()}}>{t("refresh")}</button>
                </form>
            </div>
        </div>
    );
}

export default withNamespaces()(EditOtherAccount);