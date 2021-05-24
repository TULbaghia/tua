import {withNamespaces} from "react-i18next";
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import React, {useState} from "react";
import { api } from "../Api";
import {useNotificationSuccessAndShort} from "./Notification/NotificationProvider";

function PasswordReset(props) {
    const {t,i18n} = props

    const [email, setEmail] = useState('');

    const dispatchNotificationSuccess = useNotificationSuccessAndShort();



    const handleSend = e => {
        e.preventDefault()
        api.sendResetPassword(email)
        .then(res => {
            console.log(res);
            dispatchNotificationSuccess({message: i18n.t('passwordResetRequestSendSuccess')})
        })
        .catch(err => console.log(err))
    }

    
    const handleSendAgain = e => {
        e.preventDefault()
        api.sendResetPasswordAgain(email)
        .then(res => {
            console.log(res);
            dispatchNotificationSuccess({message: i18n.t('passwordResetRequestSendSuccess')})
        })
        .catch(err => console.log(err))
    }


    return(
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item"><Link to="/login">{t('logging')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('passwordResetting')}</li>
            </BreadCrumb>

            <div className="floating-box" style={{minWidth: "30rem"}}>
                <h1 className="h3">{t('passwordResettingForm')}</h1>

                <form>
                    <div className="input-group mb-3" style={{marginTop: "3rem"}}>
                        <div className="input-group-prepend" style={{marginBottom: "2.5rem"}}>
                            <span className="input-group-text" id="basic-addon1">@</span>
                        </div>
                        <input type="text" className="form-control" placeholder="E-mail" aria-label="E-mail"
                                                       onChange={event => setEmail(event.target.value)}
                                                       aria-describedby="basic-addon1"/>
                        <span style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</span>
                    </div>

                    <button className="btn btn-lg btn-primary btn-block" onClick={handleSend} style={{backgroundColor: "#7749F8", width: "70%", margin: "auto"}}>
                        {t('send')}
                    </button>

                    <button className="btn btn-lg btn-primary btn-block" onClick={handleSendAgain} style={{backgroundColor: "#7749F8", width: "70%", margin: "auto"}}>
                        {t('sendAgain')}
                    </button>
                </form>
            </div>
        </div>
    )
}

export default withNamespaces()(PasswordReset);
