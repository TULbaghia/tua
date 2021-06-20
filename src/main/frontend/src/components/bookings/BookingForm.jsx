import React, {useEffect, useState} from "react";
import {useHistory} from "react-router";
import {withNamespaces} from "react-i18next";
import BreadCrumb from "./../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {api} from "../../Api";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort,
    useNotificationWarningAndLong,
} from "./../Utils/Notification/NotificationProvider";
import {ResponseErrorHandler} from "./../Validation/ResponseErrorHandler";
import {useThemeColor} from "./../Utils/ThemeColor/ThemeColorProvider";
import {Field, Form, Formik} from "formik";
import SelectField from "./../controls/SelectField";
import "../../css/overlay.css";
import "react-datepicker/dist/react-datepicker.css";
import DatePickerField from "../controls/DatePickerField";
import "../../css/booking-form.css";
import {useLocale} from "../LoginContext";
import DataTable from "react-data-table-component"
import {useDialogPermanentChange} from "./../Utils/CriticalOperations/CriticalOperationProvider";
import {Col, Container, Row} from "react-bootstrap";


function BookingForm(props) {
    const {t, i18n} = props;
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchNotificationWarning = useNotificationWarningAndLong();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const history = useHistory();
    const [submitting, setSubmitting] = useState(false);
    const {token, setToken} = useLocale();
    const [hotels, setHotels] = useState([])
    const [boxes, setBoxes] = useState([])
    const dispatchCriticalDialog = useDialogPermanentChange();

    const themeColor = useThemeColor();

    useEffect(() => {
        api.getAllHotelsList({headers: {Authorization: token}}).then(res => {
            const hotelList = res.data.map((el, i) => {
                return {
                    label: el.name,
                    value: el.id
                }
            })
            setHotels(hotelList)

        })
    }, [])

    const columns = [
        {
            name: t('animalType'),
            selector: 'animalType',
            sortable: true,
            width: "10rem",
            cell: row => {
                return t(row.animalType);
            }
        },
        {
            name: t('booking.description'),
            selector: 'description',
            sortable: false,
            width: "10rem"
        },
        {
            name: t('price'),
            selector: 'price',
            sortable: false,
            width: "10rem",
            cell: row => {
                return row.price + " " + t('currency');
            }
        }
    ];

    const animalTypes = [
        {
            label: "dog",
            value: 0,
        },
        {
            label: "cat",
            value: 1,
        },
        {
            label: "rodent",
            value: 2,
        },
        {
            label: "bird",
            value: 3,
        },
        {
            label: "rabbit",
            value: 4,
        },
        {
            label: "lizard",
            value: 5,
        },
        {
            label: "turtle",
            value: 6,
        },
    ];

    const tommorrow = new Date()
    tommorrow.setDate(new Date().getDate() + 1)

    const initialValues = {
        hotelId: -1,
        dateFrom: tommorrow,
        dateTo: tommorrow,
        boxes: [],
    };

    console.log(initialValues)

    function getDateString(date) {
        console.log(date)
        return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
    }

    function fetchBoxes(hotelId, dateFrom, dateTo) {
        api.getAvailableBoxesBetween(hotelId, getDateString(dateFrom), getDateString(dateTo), {headers: {Authorization: token}}).then(res => {
            setBoxes(res.data)
        })
    }

    function onSubmit(values, {resetForm}) {
        setSubmitting(true);
        const dto = {...values};

        dispatchCriticalDialog({
            message: i18n.t("booking.cancellation_limit_info"),
            callbackOnSave: () => createReservation(dto, resetForm),
            callbackOnCancel: () => {
                setSubmitting(false)
            }
        })
    }

    function createReservation(dto, resetForm) {
        api
            .addBooking(dto, {headers: {Authorization: token}})
            .then((res) => {
                dispatchNotificationSuccess({
                    message: t("booking.create.success"),
                });
                resetForm();
            })
            .catch((err) => {
                ResponseErrorHandler(err, dispatchNotificationDanger);
            })
            .finally(() => {
                setSubmitting(false);
            });
    }

    function validate(values) {
        const errors = {};
        const now = new Date();
        if (values.dateFrom >= values.dateTo) {
            errors.dateFrom = t("booking.form.error.date_not_earlier");
        }
        if (values.dateFrom < now) {
            errors.dateFrom = t("booking.date_cannot_refer_to_past")
        }
        if (values.dateTo < now) {
            errors.dateTo = t("booking.date_cannot_refer_to_past")
        }
        if (values.boxes.length === 0) {
            errors.boxes = t("booking.form.error.no_boxes")
        }
        return errors;
    }

    return (
        <div className={"container-fluid"}>
            <BreadCrumb>
                <li className="breadcrumb-item">
                    <Link to="/">{t("mainPage")}</Link>
                </li>
                <li className="breadcrumb-item active" aria-current="page">
                    {t("booking.form.bread_crumb")}
                </li>
            </BreadCrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={10} lg={8} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <Formik {...{initialValues, validate, onSubmit, submitting}}>
                            {({values, errors, touched, setFieldValue}) => (
                                <div className="pt-2 pb-2">
                                    <h3 className="h3 text-center mt-3">{t("booking.form.title")}</h3>

                                    <Form className="row g-3">
                                        <div
                                            className="overlay"
                                            style={{display: submitting ? "inline-flex" : "none"}}
                                        >
                                            <div class="spinner-border" role="status">
                                                <span class="sr-only">Loading...</span>
                                            </div>
                                        </div>

                                        <div className="col-md-12 mb-2">
                                            <label htmlFor="hotelId">{t('hotel')}</label>
                                            <Field
                                                className="col-md-5"
                                                name="hotelId"
                                                component={SelectField}
                                                options={hotels}
                                            />
                                        </div>

                                        <div className="col-md-12">
                                            <label htmlFor="dateFrom">{t('booking.form.date_from')}</label>
                                            <DatePickerField name="dateFrom"/>
                                            {errors && touched && errors.dateFrom && (
                                                <div style={{color: "red"}}>{errors.dateFrom}</div>
                                            )}
                                        </div>

                                        <div className="col-md-12">
                                            <label htmlFor="dateTo">{t('booking.form.date_to')}</label>
                                            <DatePickerField name="dateTo"/>
                                            {errors && touched && errors.dateTo && (
                                                <div style={{color: "red"}}>{errors.dateTo}</div>
                                            )}
                                        </div>

                                        <div className="col-12 my-3">
                                            <button
                                                className="btn btn-primary"
                                                style={{backgroundColor: "#7749F8"}}
                                                onClick={() => fetchBoxes(values.hotelId, values.dateFrom, values.dateTo)}
                                                type="button"
                                            >
                                                {t("booking.search_boxes")}
                                            </button>
                                        </div>

                                        <div className="col-12">
                                            <DataTable className={"rounded-0"}
                                                       noDataComponent={i18n.t('table.no.result')}
                                                       columns={columns}
                                                       noHeader={true}
                                                       data={boxes}
                                                       selectableRows
                                                       theme={themeColor}
                                                       onSelectedRowsChange={(state) => {
                                                           const ids = state.selectedRows.map(x => x.id)
                                                           setFieldValue('boxes', ids)
                                                       }}
                                            />
                                            {errors && touched && errors.boxes && (
                                                <div style={{color: "red"}}>{errors.boxes}</div>
                                            )}
                                        </div>

                                        <div className="col-12 d-flex justify-content-center my-3">
                                            <button
                                                className="btn btn-primary"
                                                style={{backgroundColor: "#7749F8"}}
                                                type="submit"
                                            >
                                                {t("booking.create_reservation")}
                                            </button>
                                        </div>
                                    </Form>
                                </div>
                            )}
                        </Formik>
                    </Col>
                </Row>
            </Container>
        </div>
    );
}

export default withNamespaces()(BookingForm);
