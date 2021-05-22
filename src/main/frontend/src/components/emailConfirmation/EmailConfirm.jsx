import React from "react";
import {useHistory} from "react-router";
import {Link, useParams} from "react-router-dom";
import { withNamespaces } from 'react-i18next';
import BreadCrumb from "../BreadCrumb"
import {Configuration, DefaultApi} from "api-client";
import {useNotification} from "../Notification/NotificationProvider";
import {dialogDuration, dialogType} from "../Notification/Notification";

function EmailConfirm({t,i18n}) {
    const dispatch = useNotification();
    const history = useHistory();
    let {code} = useParams();
    const conf = new Configuration()
    const api = new DefaultApi(conf)

    const handleConfirm = () => {
        api.confirmEmail(code).then((res) => {
            dispatch({
                dialogType: dialogType.SUCCESS,
                dialogDuration: dialogDuration.SHORT,
                message: t('emailConfirm.success.info'),
                title: t('operationSuccess')
            })
            history.push("/login");
        }).catch(err => {
            dispatch({
                dialogType: dialogType.DANGER,
                dialogDuration: dialogDuration.SHORT,
                message: t(err.response.data.message),
                title: t('operationError')
            })
        })
    }

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('emailConfirm.title')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <form className="form-signin p-0">
                    <h3 className="mb-4">{t('emailConfirm.title')}</h3>
                    <span>{t('emailConfirm.info')}</span>
                    <button className="btn btn-lg btn-primary btn-block mt-5"
                            type="button"
                            onClick={() => handleConfirm()}
                            style={{backgroundColor: "#7749F8", whiteSpace: 'normal'}}>
                        {t('emailConfirm.action')}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default withNamespaces()(EmailConfirm);
