import React, {useEffect, useState} from "react";
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {useHistory, useLocation} from "react-router";
import {Form, Formik} from "formik";
import i18n from "../../i18n";
import {withNamespaces} from "react-i18next";
import {useLocale} from "../LoginContext";
import FieldComponent from "./ModifyCityFormComponents/FieldComponent";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import './ModifyCityWrapper.scss';
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import {ModifyCityValidationSchema} from "./ModifyCityFormComponents/ValidationSchema";
import {getCity, modifyCity} from "./ModifyCityApiUtil";
import {rolesConstant} from "../../Constants";
import queryString from "query-string";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import TextAreaComponent from "./ModifyCityFormComponents/TextAreaComponent";
import {Col, Container, Row} from "react-bootstrap";
import {v4} from "uuid";

function ModifyCityForm() {
    const location = useLocation();
    const {token} = useLocale();
    const [etag, setETag] = useState()
    const [city, setCity] = useState({
        id: "",
        name: "",
        description: ""
    });

    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchDialog = useDialogPermanentChange();

    const cityId = queryString.parse(location.search).id;

    useEffect(() => {
        if (token) {
            handleFetch(true);
        }
    }, [token]);

    const handleFetch = (firstFetch = false) => {
        getCity(cityId, token).then(response => {
            setCity({...response.data, key: v4()});
            setETag(response.headers.etag);
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
        });

        if (!firstFetch) {
            dispatchNotificationSuccess({message: i18n.t('dataRefresh')});
        }
    }

    const handleCityModify = (values, setSubmitting) => {
        const falseSubmitting = () => setSubmitting(false);

        dispatchDialog({
            callbackOnSave: () => {
                modifyCity({values, token, etag})
                    .then(response => {
                        dispatchNotificationSuccess({message: i18n.t('modifyCity.success')})
                        falseSubmitting();
                    }).catch(err => {
                        ResponseErrorHandler(err, dispatchNotificationDanger)
                        falseSubmitting()
                    });
            },
            callbackOnCancel: falseSubmitting
        });
    }

    return (
        <div id="modify-city-form">
            <BreadCrumb>
                <li className="breadcrumb-item">
                    <Link to="/">{i18n.t('mainPage')}</Link>
                </li>
                <li className="breadcrumb-item">
                    <Link to="/">
                        {i18n.t('adminDashboard')}
                    </Link>
                </li>
                <li className="breadcrumb-item">
                    <Link to="/cities">
                        {i18n.t('cityList')}
                    </Link>
                </li>
                <li className="breadcrumb-item active" aria-current="page">
                    {i18n.t('modifyCity.title')}
                </li>
            </BreadCrumb>
            <div className="defloatify floating-box">
                <Container>
                    <Row className="text-center justify-content-center d-block">
                        <h1 className="mb-3">{i18n.t('modifyCity.title')}</h1>
                        <h5>{i18n.t('modifyCity.modify.info')}{city.name}</h5>
                        <button className="mt-3 w-25 btn-background-custom btn btn-primary"
                                onClick={(e) => handleFetch()}
                                type="submit">
                            {i18n.t("refresh")}
                        </button>
                        <p className="mt-2 obligatory-fields">
                            {i18n.t('obligatoryFields')}
                        </p>
                    </Row>
                    <Formik
                        initialValues={{...city}}
                        enableReinitialize
                        validate={ModifyCityValidationSchema}
                        onSubmit={(values, {setSubmitting}) => handleCityModify(values, setSubmitting)}>
                        {({isSubmitting, handleChange}) => (
                            <Form>
                                <Row>
                                    <Col sm={12}>
                                        <FieldComponent name="name"
                                                        label={i18n.t('modifyCity.modify.name')}
                                                        handleChange={handleChange}/>

                                        <TextAreaComponent name="description"
                                                           obligatory
                                                           label={i18n.t('modifyCity.modify.description')}
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

export default withNamespaces()(ModifyCityForm);
