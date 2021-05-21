import {withNamespaces} from "react-i18next";
import BreadCrumb from "../BreadCrumb";
import {Link, useParams} from "react-router-dom";
import React from "react";
import {Form, Formik} from 'formik';
import FieldComponent from "./FieldComponent";

function PasswordResetForm({t, i18n}) {
    let {code} = useParams();

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item"><Link to="/login">{t('logging')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('passwordResetFormTitle')}</li>
            </BreadCrumb>

            <div className="floating-box" style={{minWidth: "30rem"}}>
                <div>
                    <h1 className="h3 mb-4">{t('passwordResetFormTitle')}</h1>
                    <Formik
                        initialValues={{newPassword: '', repeatedNewPassword: ''}}
                        validate={values => {
                            const errors = {};
                            if (!values.newPassword) {
                                errors.newPassword = t('passwordRequired');
                            } else if (values.newPassword.length > 64 || values.newPassword.length < 8) {
                                errors.newPassword = t('passwordLengthError');
                            }

                            if (!values.repeatedNewPassword) {
                                errors.repeatedNewPassword = t('repeatedPasswordRequired');
                            } else if (values.repeatedNewPassword.length > 64 || values.repeatedNewPassword.length < 8) {
                                errors.repeatedNewPassword = t('passwordLengthError');
                            }

                            if (!(values.repeatedNewPassword === values.newPassword)) {
                                errors.repeatedNewPassword = t('passwordsDontMatch');
                            }
                            return errors;
                        }}
                        onSubmit={(values, {setSubmitting}) => {
                            console.log(values.newPassword + ", " + values.repeatedNewPassword + ", path param: " + code);
                            setSubmitting(false);
                        }}
                    >
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
