import React, {useEffect, useState} from "react";
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {useHistory, useLocation} from "react-router";
import {Form, Formik} from "formik";
import i18n from "../../i18n";
import {withNamespaces} from "react-i18next";
import {useLocale} from "../LoginContext";
import FieldComponent from "./ModifyHotelFormComponents/FieldComponent";
import SelectComponent from "./ModifyHotelFormComponents/SelectComponent";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import './ModifyHotelWrapper.scss';
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import {ModifyHotelValidationSchema} from "./ModifyHotelFormComponents/ValidationSchema";
import {getCities, getOtherHotelEtag, getOwnHotelEtag, modifyHotel, modifyOtherHotel} from "./ModifyHotelApiUtil";
import {rolesConstant} from "../../Constants";
import queryString from "query-string";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import TextAreaComponent from "./ModifyHotelFormComponents/TextAreaComponent";
import {Col, Container, Row} from "react-bootstrap";

function ModifyHotelForm() {
    const location = useLocation();
    const history = useHistory();
    const {token, currentRole} = useLocale();
    const [etag, setETag] = useState()
    const [cities, setCities] = useState([]);
    const [hotel, setHotel] = useState({
        id: "",
        name: "",
        address: "",
        cityName: "",
        image: null,
        description: ""
    });

    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchDialog = useDialogPermanentChange();
    const idFromQuery = queryString.parse(location.search).id;

    useEffect(() => {
        if (token) {
            handleFetch();
        }
    }, [token]);

    const handleFetch = (firstFetch = true) => {
        let onSuccess = res => {
            setHotel(res.data);
            setETag(res.headers.etag);
        };

        if (currentRole === rolesConstant.manager) {
            getOwnHotelEtag(token).then(onSuccess);
        } else {
            getOtherHotelEtag(idFromQuery, token)
                .then(onSuccess)
                .catch(err => {
                    history.push("/");
                    dispatchNotificationDanger({message: i18n.t('modifyHotel.error.id_invalid')});
                });
        }

        getCities(token).then(res => {
            setCities(res.data);
        }).catch(err => {
            history.push("/");
            ResponseErrorHandler(err, dispatchNotificationDanger);
        });

        if (!firstFetch) {
            dispatchNotificationSuccess({message: i18n.t('dataRefresh')});
        }
    }

    const handleHotelModify = (values, setSubmitting) => {
        let id = Number(hotel.id);

        dispatchDialog({
            callbackOnSave: () => {
                if (currentRole === rolesConstant.manager) {
                    modifyHotel({values, token, etag})
                        .then(res => {
                            dispatchNotificationSuccess({message: i18n.t('modifyHotel.success')})
                            history.push("/");
                        })
                        .catch(err => ResponseErrorHandler(err, dispatchNotificationDanger));
                } else {
                    modifyOtherHotel({id, values, token, etag})
                        .then(res => {
                            dispatchNotificationSuccess({message: i18n.t('modifyHotel.success')})
                            history.push("/hotels");
                        })
                        .catch(err => ResponseErrorHandler(err, dispatchNotificationDanger));
                }
            },
            callbackOnCancel: () => {
            }
        });
        setSubmitting(false);
    }

    return (
        <div id="modify-hotel-form" className={"mb-2 container-fluid"}>
            <BreadCrumb>
                <li className="breadcrumb-item">
                    <Link to="/">{i18n.t('mainPage')}</Link>
                </li>
                <li className="breadcrumb-item">
                    <Link to="/">
                        {currentRole === rolesConstant.manager ? i18n.t('managerDashboard') : i18n.t('adminDashboard')}
                    </Link>
                </li>
                {currentRole === rolesConstant.admin &&
                    <li className="breadcrumb-item">
                        <Link to="/hotels">{i18n.t('hotelList')}</Link>
                    </li>
                }
                <li className="breadcrumb-item active" aria-current="page">
                    {i18n.t('modifyHotel.title')}
                </li>
            </BreadCrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={10} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div className="">
                            <div>
                                <Row className="text-center justify-content-center d-block">
                                    <h1 className="mb-3">{i18n.t('modifyHotel.title')}</h1>
                                    <h5>{i18n.t('modifyHotel.modify.info')}{hotel.name}</h5>
                                    <button className="my-3 w-25 btn btn-secondary"
                                            onClick={(e) => handleFetch(false)}
                                            type="submit">
                                        {i18n.t("refresh")}
                                    </button>
                                    <p className="obligatory-fields">
                                        {i18n.t('obligatoryFields')}
                                    </p>
                                </Row>
                                <Formik
                                    initialValues={{
                                        name: hotel.name,
                                        address: hotel.address,
                                        city: cities.filter(x => x.name === hotel.cityName).map(x => x.id)[0],
                                        image: hotel.image,
                                        description: hotel.description,
                                    }}
                                    enableReinitialize
                                    validate={ModifyHotelValidationSchema}
                                    onSubmit={(values, {setSubmitting}) => handleHotelModify(values, setSubmitting)}>
                                    {({isSubmitting, handleChange, errors}) => (
                                        <Form>
                                            <Row>
                                                <Col sm={6}>
                                                    <FieldComponent name="name"
                                                                    obligatory
                                                                    label={i18n.t('modifyHotel.modify.name')}
                                                                    handleChange={handleChange}/>
                                                    <FieldComponent name="address"
                                                                    obligatory
                                                                    label={i18n.t('modifyHotel.modify.address')}
                                                                    handleChange={handleChange}/>
                                                    <SelectComponent name="city"
                                                                     obligatory
                                                                     entryValue={hotel.cityName}
                                                                     label={i18n.t('modifyHotel.modify.city')}
                                                                     values={cities}
                                                                     handleChange={handleChange}/>
                                                </Col>
                                                <Col sm={6}>
                                                    <TextAreaComponent name="description"
                                                                       obligatory
                                                                       label={i18n.t('modifyHotel.modify.description')}
                                                                       handleChange={handleChange}/>
                                                    <FieldComponent name="image"
                                                                    placeholder="/static/media/example.jpg"
                                                                    label={i18n.t('modifyHotel.modify.image')}
                                                                    handleChange={handleChange}/>
                                                </Col>
                                            </Row>
                                            <Row>
                                                <button className="btn-background-custom btn btn-lg btn-primary mt-3"
                                                        type="submit"
                                                        disabled={isSubmitting || Object.keys(errors).length > 0}>
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

export default withNamespaces()(ModifyHotelForm);
