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

function ModifyHotelForm() {
    const location = useLocation();
    const history = useHistory();
    const {token, currentRole} = useLocale();
    const [etag, setETag] = useState()
    const [cities, setCities] = useState([]);
    const [hotel, setHotel] = useState({id: "", name: "", address: "", cityName: ""});

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
        let onSuccess = res => {
            dispatchNotificationSuccess({message: i18n.t('modifyHotel.success')})
            history.push("/");
        }

        dispatchDialog({
            callbackOnSave: () => {
                if (currentRole === rolesConstant.manager) {
                    modifyHotel({values, token, etag})
                        .then(onSuccess)
                        .catch(err => ResponseErrorHandler(err, dispatchNotificationDanger));
                } else {
                    modifyOtherHotel({id, values, token, etag})
                        .then(onSuccess)
                        .catch(err => ResponseErrorHandler(err, dispatchNotificationDanger));
                }
            },
            callbackOnCancel: () => {
            }
        });
        setSubmitting(false);
    }

    return (
        <div id="modify-hotel-container">
            <BreadCrumb>
                <li className="breadcrumb-item">
                    <Link to="/">{i18n.t('mainPage')}</Link>
                </li>
                <li className="breadcrumb-item">
                    <Link to="/">
                        {currentRole === rolesConstant.manager ? i18n.t('managerDashboard') : i18n.t('adminDashboard')}
                    </Link>
                </li>
                <li className="breadcrumb-item active" aria-current="page">{i18n.t('modifyHotel.title')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <div className="d-block w-100 text-center">
                    <h1 className="mb-3">{i18n.t('modifyHotel.title')}</h1>
                    <h5>{i18n.t('modifyHotel.modify.info')}{hotel.name}</h5>
                    <button className="w-50 btn-background-custom btn btn-primary my-3"
                            onClick={(e) => handleFetch(false)}
                            type="submit">
                        {i18n.t("refresh")}
                    </button>
                    <p className="obligatory-fields">{i18n.t('obligatoryFields')}</p>
                </div>
                <Formik
                    initialValues={{
                        name: hotel.name,
                        address: hotel.address,
                        city: cities.filter(x => x.name === hotel.cityName).map(x => x.id)[0]
                    }}
                    enableReinitialize
                    validate={ModifyHotelValidationSchema}
                    onSubmit={(values, {setSubmitting}) => handleHotelModify(values, setSubmitting)}>
                    {({isSubmitting, handleChange}) => (
                        <Form className="container">
                            <FieldComponent name="name"
                                            label={i18n.t('modifyHotel.modify.name')}
                                            handleChange={handleChange}/>
                            <FieldComponent name="address"
                                            label={i18n.t('modifyHotel.modify.address')}
                                            handleChange={handleChange}/>
                            <SelectComponent name="city"
                                             entryValue={hotel.cityName}
                                             label={i18n.t('modifyHotel.modify.city')}
                                             values={cities}
                                             handleChange={handleChange}/>
                            <div className="d-flex w-100 justify-content-center">
                                <button className="btn-background-custom btn btn-lg btn-primary mt-3"
                                        type="submit"
                                        disabled={isSubmitting}>
                                    {i18n.t('send')}
                                </button>
                            </div>
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    )
}

export default withNamespaces()(ModifyHotelForm);
