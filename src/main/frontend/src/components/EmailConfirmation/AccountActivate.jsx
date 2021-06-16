import React from "react";
import {useHistory} from "react-router";
import {Link, useParams} from "react-router-dom";
import { withNamespaces } from 'react-i18next';
import BreadCrumb from "../Partial/BreadCrumb"
import {Configuration, DefaultApi} from "ssbd06-api";
import {useNotificationCustom} from "../Utils/Notification/NotificationProvider";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import {dialogDuration, dialogType} from "../Utils/Notification/Notification";
import i18n from "../../i18n";

function AccountActivate() {
    const dispatchNotification = useNotificationCustom();
    const dispatchCriticalDialog = useDialogPermanentChange();
    const history = useHistory();
    let {code} = useParams();
    const conf = new Configuration()
    const api = new DefaultApi(conf)

    const handleConfirmation = () => (
        dispatchCriticalDialog({
            callbackOnSave: () => handleSubmit(),
            callbackOnCancel: () => {}
        })
    )

    const handleSubmit = () => {
        api.confirm(code).then((res) => {
            dispatchNotification({
                dialogType: dialogType.SUCCESS,
                dialogDuration: dialogDuration.SHORT,
                message: i18n.t('accountActivate.success.info'),
                title: i18n.t('operationSuccess')
            })
            history.push("/login");
        }).catch(err => {
            dispatchNotification({
                dialogType: dialogType.DANGER,
                dialogDuration: dialogDuration.SHORT,
                message: i18n.t(err.response.data.message),
                title: i18n.t('operationError')
            })
        })
    }

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{i18n.t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{i18n.t('accountActivate.title')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <form className="form-signin p-0">
                    <h3 className="mb-4">{i18n.t('accountActivate.title')}</h3>
                    <span>{i18n.t('accountActivate.info')}</span>
                    <button className="btn btn-lg btn-primary btn-block mt-5"
                            type="button"
                            onClick={() => handleConfirmation()}
                            style={{backgroundColor: "#7749F8", whiteSpace: 'normal'}}>
                        {i18n.t('accountActivate.action')}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default withNamespaces()(AccountActivate);
