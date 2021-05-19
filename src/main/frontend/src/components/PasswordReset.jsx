import {withNamespaces} from "react-i18next";
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import React from "react";

function PasswordReset(props) {
    const {t,i18n} = props

    const handleSend = e => {
        e.preventDefault()
        console.log("Sending email");
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

                <form onSubmit={handleSend}>
                    <div className="input-group mb-3" style={{marginTop: "3rem"}}>
                        <div className="input-group-prepend" style={{marginBottom: "2.5rem"}}>
                            <span className="input-group-text" id="basic-addon1">@</span>
                        </div>
                        <input type="text" className="form-control" placeholder="E-mail" aria-label="E-mail"
                               aria-describedby="basic-addon1"/>
                        <span style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</span>
                    </div>

                    <button className="btn btn-lg btn-primary btn-block" type="submit" style={{backgroundColor: "#7749F8", width: "70%", margin: "auto"}}>
                        {t('send')}
                    </button>
                </form>
            </div>
        </div>
    )
}

export default withNamespaces()(PasswordReset);
