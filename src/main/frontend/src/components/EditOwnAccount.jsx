import React, {useState} from "react";
import {useHistory} from "react-router";
import {useLocale} from "./LoginContext";
import {withNamespaces} from 'react-i18next';
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import {Configuration, DefaultApi} from "api-client";
import {
    useNotificationDangerAndLong,
    useNotificationSuccessAndShort,
    useNotificationWarningAndLong,
} from "./Notification/NotificationProvider";
import {useDialogPermanentChange} from "./CriticalOperations/CriticalOperationProvider";
import i18n from '../i18n';


function EditOwnAccount() {
    const history = useHistory();
    const {token, setToken} = useLocale();
    const [etag, setETag] = useState();
    const [email, setEmail] = useState('');
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [repeatedPassword, setRepeatedPassword] = useState();
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [contactNumber, setContactNumber] = useState('');

    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationWarning = useNotificationWarningAndLong();
    const dispatchNotificationDanger = useNotificationDangerAndLong();
    const dispatchDialog = useDialogPermanentChange();

    const conf = new Configuration();
    const api = new DefaultApi(conf);


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
                callbackOnSave: () => {editEmail()},
                callbackOnCancel: () => {console.log("Cancel")},
            })
        }
    }

    const editEmail = () => {
        api.editOwnAccountEmail({newEmail: email}, {
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
                callbackOnSave: () => {editPassword()},
                callbackOnCancel: () => {console.log("Cancel")},
            })
        }
    }

    const editPassword = () => {
        api.changePassword({oldPassword: oldPassword, newPassword: newPassword}, {
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
        if (name.length < 3 || name.length > 31) {
            dispatchNotificationWarning({message: i18n.t('nameSize')})
        } else if (surname.length < 2 || surname.length > 31) {
            dispatchNotificationWarning({message: i18n.t('surnameSize')})
        } else if (contactNumber.length < 9 || contactNumber.length > 15) {
            dispatchNotificationWarning({message: i18n.t('phoneSize')})
        } else {
            dispatchDialog({
                callbackOnSave: () => {editDetails()},
                callbackOnCancel: () => {console.log("Cancel")},
            })
        }
    }

    const editDetails = () => {
        api.editOwnAccountDetails({firstname: name, lastname: surname, contactNumber: contactNumber}, {
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

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{i18n.t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{i18n.t('signUp')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <form className="form-signup">
                    <h1 className="h3">{i18n.t('editProfile')}</h1>
                    <input
                        id="email"
                        className="form-control"
                        placeholder={i18n.t('emailAddress')}
                        autoFocus={true}
                        onChange={event => setEmail(event.target.value)}
                        style={{marginTop: "1.5rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <button className="btn btn-lg btn-primary btn-block" onClick={handleEmailSubmit} type="submit"
                            style={{backgroundColor: "#7749F8"}}>
                        {i18n.t('changeEmail')}
                    </button>
                    <input
                        id="oldPassword"
                        className="form-control"
                        type="password"
                        placeholder={i18n.t('oldPassword')}
                        autoFocus={true}
                        onChange={event => setOldPassword(event.target.value)}
                        style={{marginTop: "2rem", width: "90%", display: "inline-block"}}
                    />
                    <input
                        id="newPassword"
                        className="form-control"
                        type="password"
                        placeholder={i18n.t('newPassword')}
                        autoFocus={true}
                        onChange={event => setNewPassword(event.target.value)}
                        style={{marginTop: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <input
                        id="repeatNewPassword"
                        className="form-control"
                        type="password"
                        placeholder={i18n.t('repeatNewPassword')}
                        autoFocus={true}
                        onChange={event => setRepeatedPassword(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <button className="btn btn-lg btn-primary btn-block" onClick={handlePasswordSubmit} type="submit"
                            style={{backgroundColor: "#7749F8"}}>
                        {i18n.t('changePassword')}
                    </button>
                    <input
                        id="name"
                        className="form-control"
                        placeholder={i18n.t('name')}
                        autoFocus={true}
                        onChange={event => setName(event.target.value)}
                        style={{marginTop: "2rem", width: "90%", display: "inline-block"}}
                    />
                    <input
                        id="surname"
                        className="form-control"
                        placeholder={i18n.t('surname')}
                        autoFocus={true}
                        onChange={event => setSurname(event.target.value)}
                        style={{marginTop: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <input
                        id="phoneNumber"
                        className="form-control"
                        placeholder={i18n.t('phoneNumber')}
                        autoFocus={true}
                        onChange={event => setContactNumber(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <button className="btn btn-lg btn-primary btn-block" onClick={handleDetailsSubmit} type="submit"
                            style={{backgroundColor: "#7749F8"}}>
                        {i18n.t('changeDetails')}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default withNamespaces()(EditOwnAccount);