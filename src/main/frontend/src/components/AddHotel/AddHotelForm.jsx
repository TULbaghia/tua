import React, {useEffect, useState} from "react";
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {useHistory} from "react-router";
import {Form, Formik} from "formik";
import i18n from "../../i18n";
import {withNamespaces} from "react-i18next";
import {useLocale} from "../LoginContext";
import FieldComponent from "./AddHotelFormComponents/FieldComponent";
import SelectComponent from "./AddHotelFormComponents/SelectComponent";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import './AddHotelWrapper.scss';
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import {AddHotelValidationSchema} from "./AddHotelFormComponents/ValidationSchema";
import {addHotel, getCities} from "./AddHotelApiUtil";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import TextAreaComponent from "./AddHotelFormComponents/TextAreaComponent";
import {Col, Container, Row} from "react-bootstrap";

function AddHotelForm() {
    const history = useHistory();
    const {token} = useLocale();
    const [cities, setCities] = useState([]);

    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchDialog = useDialogPermanentChange();

    useEffect(() => {
        if (token) {
            handleFetch();
        }
    }, [token]);

    const handleFetch = (firstFetch = true) => {
        getCities(token).then(res => {
            setCities(res.data);
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
        });

        if (!firstFetch) {
            dispatchNotificationSuccess({message: i18n.t('dataRefresh')});
        }
    }

    const handleHotelAdd = (values, setSubmitting) => {
        let onSuccess = res => {
            dispatchNotificationSuccess({message: i18n.t('addHotel.success')})
            history.push('/hotels')
        }

        dispatchDialog({
            callbackOnSave: () => {
                addHotel({values, token})
                    .then(onSuccess)
                    .catch(err => ResponseErrorHandler(err, dispatchNotificationDanger));

            },
        });
        setSubmitting(false);
    }

    return (
        <div id="add-hotel-form" className={"container-fluid"}>
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
                    <Link to="/hotels">
                        {i18n.t('hotelList')}
                    </Link>
                </li>
                <li className="breadcrumb-item active" aria-current="page">
                    {i18n.t('addHotel.title')}
                </li>
            </BreadCrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={10} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <Row className="text-center justify-content-center d-block">
                            <h1 className="mb-3">{i18n.t('addHotel.title')}</h1>
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
                                name: "",
                                address: "",
                                city: cities.map(x => x.id)[0],
                                image: null,
                                description: "",
                            }}
                            initialErrors={{
                                dummy: ""
                            }}
                            enableReinitialize
                            validate={AddHotelValidationSchema}
                            onSubmit={(values, {setSubmitting}) => handleHotelAdd(values, setSubmitting)}>
                            {({isSubmitting, handleChange, errors}) => (
                                <Form>
                                    <Row>
                                        <Col sm={6}>
                                            <FieldComponent name="name"
                                                            obligatory
                                                            label={i18n.t('addHotel.add.name')}
                                                            handleChange={handleChange}/>
                                            <FieldComponent name="address"
                                                            obligatory
                                                            label={i18n.t('addHotel.add.address')}
                                                            handleChange={handleChange}/>
                                            <SelectComponent name="city"
                                                             obligatory
                                                             entryValue={cities.map(x => x.name)[0]}
                                                             label={i18n.t('addHotel.add.city')}
                                                             values={cities}
                                                             handleChange={handleChange}/>
                                        </Col>
                                        <Col sm={6} className={"addHotelRight"}>
                                            <TextAreaComponent name="description"
                                                               obligatory
                                                               label={i18n.t('addHotel.add.description')}
                                                               handleChange={handleChange}/>
                                            <FieldComponent name="image"
                                                            placeholder="/static/media/example.jpg"
                                                            label={i18n.t('addHotel.add.image')}
                                                            handleChange={handleChange}/>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <button className="btn-background-custom btn btn-lg btn-primary mt-3"
                                                type="submit"
                                                disabled={isSubmitting || Object.keys(errors).length > 0}>
                                            {i18n.t('addHotel.form.add')}
                                        </button>
                                    </Row>
                                </Form>
                            )}
                        </Formik>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default withNamespaces()(AddHotelForm);
