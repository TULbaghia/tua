import React from "react";
import {useHistory} from "react-router";
import {withNamespaces} from "react-i18next";
import {Configuration, DefaultApi} from "ssbd06-api";
import {Link, useParams} from "react-router-dom";
import {Form, Formik} from 'formik';
import FieldComponent from "./FieldComponent";
import BreadCrumb from "../Partial/BreadCrumb";
import {
    useNotificationCustom,
    useNotificationDangerAndInfinity,
} from "../Utils/Notification/NotificationProvider";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import {dialogDuration, dialogType} from "../Utils/Notification/Notification";
import {dispatchErrors, ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {validatorFactory, ValidatorType} from "../Validation/Validators";

function PasswordResetForm({t, i18n}) {
    const dispatchNotification = useNotificationCustom();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchCriticalDialog = useDialogPermanentChange();
    const history = useHistory();
    let {code} = useParams();
    const conf = new Configuration()
    const api = new DefaultApi(conf)


    const handleConfirmation = (values, setSubmitting) => (
        dispatchCriticalDialog({
            callbackOnSave: () => handleSubmit(values, setSubmitting),
            callbackOnCancel: () => setSubmitting(false)
            })
    )

    const handleSubmit = (values, setSubmitting) => {
        api.resetPassword({password: values.newPassword, resetCode: code}).then((res) => {
            dispatchNotification({
                dialogType: dialogType.SUCCESS,
                dialogDuration: dialogDuration.SHORT,
                message: t('passwordResetForm.success.info'),
                title: t('operationSuccess')
            })
            history.push("/login");
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger, false, (error) => {
                dispatchErrors(error, dispatchNotificationDanger);
            });
            setSubmitting(false);
        });
    }

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('passwordResetForm.error.title')}</li>
            </BreadCrumb>

            <div className="floating-box" style={{minWidth: "30rem"}}>
                <div>
                    <h1 className="h3 mb-4">{t('passwordResetForm.error.title')}</h1>
                    <Formik
                        initialValues={{newPassword: '', repeatedNewPassword: ''}}
                        validate={values => {
                            const errors = {};
                            if (!values.newPassword) {
                                errors.newPassword = t('passwordResetForm.error.required');
                            } else {
                                validatorFactory(values.newPassword, ValidatorType.PASSWORD).forEach(x => {
                                    errors.newPassword = x;
                                })
                            }

                            if (!values.repeatedNewPassword) {
                                errors.repeatedNewPassword = t('passwordResetForm.error.repeated.required');
                            } else {
                                validatorFactory(values.repeatedNewPassword, ValidatorType.PASSWORD).forEach(x => {
                                    errors.repeatedNewPassword = x;
                                })
                            }

                            if (!(values.repeatedNewPassword === values.newPassword)) {
                                errors.repeatedNewPassword = t('passwordResetForm.error.match');
                            }
                            return errors;
                        }}
                        onSubmit={(values, {setSubmitting}) => handleConfirmation(values, setSubmitting)}>
                        {({isSubmitting, handleChange}) => (
                            <Form className={{alignItems: "center"}}>
                                <FieldComponent name="newPassword" placeholder={t('password')}
                                                handleChange={handleChange}/>
                                <FieldComponent name="repeatedNewPassword" placeholder={t('repeatPassword')}
                                                handleChange={handleChange}/>
                                <button className="btn btn-lg btn-primary btn-block mt-2"
                                        type="submit" disabled={isSubmitting}
                                        style={{backgroundColor: "#7749F8", width: "70%", margin: "auto"}}>
                                    {t('send')}
                                </button>
                            </Form>
                        )}
                    </Formik>
                </div>
            </div>
        </div>
    )
}

export default withNamespaces()(PasswordResetForm);
