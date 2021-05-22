import React from "react";
import {useHistory} from "react-router";
import {Link, useParams} from "react-router-dom";
import { withNamespaces } from 'react-i18next';
import BreadCrumb from "../BreadCrumb"
import {Configuration, DefaultApi} from "api-client";
import {useNotificationCustom} from "../Notification/NotificationProvider";
import {useDialogPermanentChange} from "../CriticalOperations/CriticalOperationProvider";
import {dialogDuration, dialogType} from "../Notification/Notification";
import i18n from "../../i18n";

function EmailConfirm() {
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
        api.confirmEmail(code).then((res) => {
            dispatchNotification({
                dialogType: dialogType.SUCCESS,
                dialogDuration: dialogDuration.SHORT,
                message: i18n.t('emailConfirm.success.info'),
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
                <li className="breadcrumb-item active" aria-current="page">{i18n.t('emailConfirm.title')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <form className="form-signin p-0">
                    <h3 className="mb-4">{i18n.t('emailConfirm.title')}</h3>
                    <span>{i18n.t('emailConfirm.info')}</span>
                    <button className="btn btn-lg btn-primary btn-block mt-5"
                            type="button"
                            onClick={() => handleConfirmation()}
                            style={{backgroundColor: "#7749F8", whiteSpace: 'normal'}}>
                        {i18n.t('emailConfirm.action')}
                    </button>
                </form>
            </div>
        </div>
    );
}

export default withNamespaces()(EmailConfirm);
