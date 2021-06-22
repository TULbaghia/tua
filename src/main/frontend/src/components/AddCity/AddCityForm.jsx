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
import TextAreaComponent from "./AddCityFormComponents/TextAreaComponent";

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
                    history.push("/cities");
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
        <div id="add-city-container" className={"container-fluid"}>
            <BreadCrumb>
                <li className="breadcrumb-item">
                    <Link to="/">{i18n.t('mainPage')}</Link>
                </li>
                <li className="breadcrumb-item">
                    <Link to="/">{i18n.t('adminDashboard')}</Link>
                </li>
                <li className="breadcrumb-item" aria-current="page">
                    <Link to="/cities">{i18n.t('cities.list.bread_crumb')}</Link>
                </li>
                <li className="breadcrumb-item active" aria-current="page">
                    {i18n.t('addCity.title')}
                </li>
            </BreadCrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={8} md={7} lg={6} className="floating-no-absolute py-4 mx-auto mb-2">
                        <div>
                            <Col xs={12} className="text-center justify-content-center d-block">
                                <h1 className="mb-3">{i18n.t('addCity.title')}</h1>
                                <p className="obligatory-fields mb-3">{i18n.t('obligatoryFields')}</p>
                            </Col>
                            <Formik
                                initialValues={{
                                    name: "",
                                    description: "",
                                }}
                                initialErrors={{
                                    dummy: ""
                                }}
                                validate={AddCityValidationSchema}
                                onSubmit={(values, {setSubmitting}) => handleAddCity(values, setSubmitting)}>
                                {({isSubmitting, handleChange, errors}) => (
                                    <Form>
                                        <Row>
                                            <Col sm={12}>
                                                <FieldComponent name="name"
                                                                label={i18n.t('addCity.name')}
                                                                placeholder={i18n.t('addCity.name')}
                                                                handleChange={handleChange}/>
                                                <TextAreaComponent name="description"
                                                                   obligatory
                                                                   label={i18n.t('addCity.description')}
                                                                   placeholder={i18n.t('addCity.description')}
                                                                   handleChange={handleChange}/>
                                            </Col>
                                        </Row>
                                        <Row>
                                            <button className="btn-background-custom btn btn-lg btn-primary mt-3"
                                                    type="submit"
                                                    disabled={isSubmitting || Object.keys(errors).length > 0}>
                                                {i18n.t('addCity.send.form')}
                                            </button>
                                        </Row>
                                    </Form>
                                )}
                            </Formik>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default withNamespaces()(AddCityForm);
