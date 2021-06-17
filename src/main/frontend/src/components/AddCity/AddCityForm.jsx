import React from "react";
import {useLocale} from "../LoginContext";
import i18n from "../../i18n";
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {Form, Formik} from "formik";
import {withNamespaces} from "react-i18next";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort,
} from "../Utils/Notification/NotificationProvider";
import FieldComponent from "./AddCityFormComponents/FieldComponent";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import {AddCityValidationSchema} from "./AddCityFormComponents/ValidationSchema";
import {addCity} from "./AddCityApiUtil";
import './AddCityFormWrapper.scss';
import {Col, Container, Row} from "react-bootstrap";
import {useHistory} from "react-router";

function AddCityForm() {
    const history = useHistory();
    const {token} = useLocale();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchDialog = useDialogPermanentChange();

    const handleAddCity = (values, setSubmitting) => {
        dispatchDialog({
            callbackOnSave: () => {
                addCity({values, token}).then(res => {
                    dispatchNotificationSuccess({message: i18n.t('addCity.success')});
                    history.push("/");
                }).catch(err => {
                    ResponseErrorHandler(err, dispatchNotificationDanger);
                });
            },
            callbackOnCancel: () => {
                setSubmitting(false)
            },
        });
        setSubmitting(false);
    }

    return (
        <div id="add-city-container">
            <BreadCrumb>
                <li className="breadcrumb-item">
                    <Link to="/">{i18n.t('mainPage')}</Link>
                </li>
                <li className="breadcrumb-item">
                    <Link to="/">{i18n.t('adminDashboard')}</Link>
                </li>
                <li className="breadcrumb-item active" aria-current="page">
                    {i18n.t('addCity.title')}
                </li>
            </BreadCrumb>
            <div className="floating-box">
                <Container>
                    <Row className="text-center justify-content-center d-block">
                        <h1 className="mb-3">{i18n.t('addCity.title')}</h1>
                        <p className="obligatory-fields">{i18n.t('obligatoryFields')}</p>
                    </Row>
                    <Formik
                        initialValues={{
                            name: "",
                            description: "",
                        }}
                        validate={AddCityValidationSchema}
                        onSubmit={(values, {setSubmitting}) => handleAddCity(values, setSubmitting)}>
                        {({isSubmitting, handleChange}) => (
                            <Form>
                                <Row>
                                    <Col>
                                        <FieldComponent name="name"
                                                        label={i18n.t('addCity.name')}
                                                        placeholder={i18n.t('addCity.name')}
                                                        handleChange={handleChange}/>
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        <FieldComponent name="description"
                                                        label={i18n.t('addCity.description')}
                                                        placeholder={i18n.t('addCity.description')}
                                                        handleChange={handleChange}/>
                                    </Col>
                                </Row>
                                <Row>
                                    <button className="btn-background-custom btn btn-lg btn-primary mt-3"
                                            type="submit"
                                            disabled={isSubmitting}>
                                        {i18n.t('send')}
                                    </button>
                                </Row>
                            </Form>
                        )}
                    </Formik>
                </Container>
            </div>
        </div>
    )
}

export default withNamespaces()(AddCityForm);
