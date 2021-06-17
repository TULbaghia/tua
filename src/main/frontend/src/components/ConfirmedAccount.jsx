import React, {useState} from "react";
import {withNamespaces} from "react-i18next";
import {useHistory} from "react-router";


function ConfirmedAccount(props) {
    const {t,i18n} = props
    const history = useHistory();

    return (
        <div className="container">
            <div className="floating-box" style={{minWidth: "30rem"}}>
                <h1 className="h3">{t('congratulations')}</h1>
                <p style={{fontSize: 16}}>{t('accountConfirmedMsg')}</p>
                <section className="button-section" style={{display: "inline-block", marginTop: "4rem"}}>
                    <button className="btn btn-primary" onClick={() => history.push("/")} style={{backgroundColor: "#7749F8", margin: "auto", marginRight: "3rem"}}>
                        {t('mainPage')}
                    </button>
                    <button className="btn btn-primary" onClick={() => history.push("/login")} style={{backgroundColor: "#7749F8", margin: "auto", marginLeft: "3rem"}}>
                        {t('signIn')}
                    </button>
                </section>
            </div>
        </div>
    );

}

export default withNamespaces()(ConfirmedAccount);