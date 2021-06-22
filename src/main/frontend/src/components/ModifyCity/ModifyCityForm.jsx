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
import {deleteCity, getCity, modifyCity} from "./ModifyCityApiUtil";
import queryString from "query-string";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import TextAreaComponent from "./ModifyCityFormComponents/TextAreaComponent";
import {Col, Container, Row} from "react-bootstrap";
import {v4} from "uuid";

function ModifyCityForm() {
    const location = useLocation();
    const history = useHistory();
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

    const handleCityDelete = () => {
        dispatchDialog({
            callbackOnSave: () => {
                const id = city.id
                deleteCity({id, token, etag})
                    .then(response => {
                        dispatchNotificationSuccess({message: i18n.t('city.delete.success')})
                        history.push("/cities");
                    }).catch(err => {
                    ResponseErrorHandler(err, dispatchNotificationDanger)
                });
            },
        });
    }

    const handleCityModify = (values, setSubmitting) => {
        const falseSubmitting = () => setSubmitting(false);

        dispatchDialog({
            callbackOnSave: () => {
                modifyCity({values, token, etag})
                    .then(response => {
                        dispatchNotificationSuccess({message: i18n.t('modifyCity.success')})
                        falseSubmitting();
                        history.push("/cities");
                    }).catch(err => {
                    ResponseErrorHandler(err, dispatchNotificationDanger)
                    falseSubmitting()
                });
            },
            callbackOnCancel: falseSubmitting
        });
    }

    return (
        <div id="modify-city-form" className={"mb-2 container-fluid"}>
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
            <Container>
                <Row>
                    <Col xs={12} sm={8} md={7} lg={6} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div>
                            <div>
                                <Row className="text-center justify-content-center d-block">
                                    <h1 className="mb-3">{i18n.t('modifyCity.title')}</h1>
                                    <h5>{i18n.t('modifyCity.modify.info')}{city.name}</h5>
                                    <button className="mt-3 mx-3 w-25 btn-background-custom btn btn-primary"
                                            onClick={(e) => handleFetch()}
                                            type="submit">
                                        {i18n.t("refresh")}
                                    </button>
                                    <button className="mt-3 mx-3 w-25 btn-background-custom btn btn-primary"
                                            onClick={(e) => handleCityDelete()}
                                            type="submit">
                                        {i18n.t("delete")}
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
                            </div>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default withNamespaces()(ModifyCityForm);
