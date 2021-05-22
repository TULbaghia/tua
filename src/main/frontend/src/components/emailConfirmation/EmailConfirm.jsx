import React from "react";
import {useHistory} from "react-router";
import {Link, useParams} from "react-router-dom";
import { withNamespaces } from 'react-i18next';
import BreadCrumb from "../BreadCrumb"
import {Configuration, DefaultApi} from "api-client";

function EmailConfirm({t,i18n}) {
    const history = useHistory();
    let {code} = useParams();
    const conf = new Configuration()
    const api = new DefaultApi(conf)

    const handleConfirm = () => {
        api.confirmEmail(code).then((res) => {
            history.push("/login");
        }).catch(err => {
            console.log("code: " + err.response.status + ", i18n key: " + err.response.data.message);
        })
    }

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('emailVerification')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <form className="form-signin p-0">
                    <h3 className="mb-4">{t('emailVerification')}</h3>
                    <span>{t('emailVerificationInfo')}</span>
                    <button className="btn btn-lg btn-primary btn-block mt-5"
                            type="button"
                            onClick={() => handleConfirm()}
                            style={{backgroundColor: "#7749F8", whiteSpace: 'normal'}}>
                        {t('emailVerificationAction')}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default withNamespaces()(EmailConfirm);
