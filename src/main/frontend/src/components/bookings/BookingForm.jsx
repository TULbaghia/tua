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
import {Checkbox, Button} from "react-bootstrap";
import {useDialogPermanentChange} from "./../Utils/CriticalOperations/CriticalOperationProvider";
import {da} from "date-fns/locale";
import DataTableField from "./../controls/DataTableField";
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
    const [clearRows, setClearRows] = useState(false)
    const [showTable, setShowTable] = useState(false)

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
            width: "15rem"
        },
        {
            name: t('price'),
            selector: 'price',
            sortable: false,
            width: "10rem",
            cell: row => {
                return row.price.toFixed(2) + " " + t('currency');
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

    let tommorrow = new Date()
    tommorrow.setDate(new Date().getDate() + 1)
    tommorrow = new Date(tommorrow.setHours(12, 0, 0, 0))

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

    function roundDate(timeStamp) {
        timeStamp -= timeStamp % (24 * 60 * 60 * 1000);//subtract amount of time since midnight
        timeStamp += new Date().getTimezoneOffset() * 60 * 1000;//add on the timezone offset
        return new Date(timeStamp);
    }

    function fetchBoxes(hotelId, dateFrom, dateTo) {
        api.getAvailableBoxesBetween(hotelId, getDateString(dateFrom), getDateString(dateTo), {headers: {Authorization: token}}).then(res => {
            setBoxes(res.data)
            setShowTable(true)
        })
    }

    function onSubmit(values, {resetForm}) {
        setSubmitting(true);
        let dto = {...values};
        // dto.dateFrom = getDateString(values.dateFrom)
        // dto.dateTo = getDateString(values.dateTo)
        console.log(dto)
        console.log(values)
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
                resetTable();
            })
            .catch((err) => {
                ResponseErrorHandler(err, dispatchNotificationDanger);
            })
            .finally(() => {
                setSubmitting(false);
            });
    }

    function resetTable() {
        setClearRows(!clearRows)
        setShowTable(false)
        setBoxes([])
    }

    function validate(values) {
        console.log("validation")
        const errors = {};
        let valid = true
        const now = new Date(tommorrow)
        console.log(values)
        if (values.dateFrom >= values.dateTo) {
            errors.dateFrom = t("booking.form.error.date_not_earlier");
            valid = false
        }
        if (values.dateFrom < now) {
            errors.dateFrom = t("booking.date_cannot_refer_to_past")
            valid = false
        }
        if (values.dateTo < now) {
            errors.dateTo = t("booking.date_cannot_refer_to_past")
            valid = false
        }
        if (values.boxes.length === 0) {
            errors.boxes = t("booking.form.error.no_boxes")
            valid = false
        }
        console.log(errors)
        if (valid) return undefined
        else return errors;
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
                        <Formik {...{initialValues, validate, onSubmit, submitting, isInitialValid: false}}>
                            {({values, errors, touched, setFieldValue, isValid}) => (
                                <div>
                                    <div className="pb-2">
                                        <h3 className="h3 text-center mb-3">{t("booking.form.title")}</h3>

                                        <p>{t('booking.form.disclaimer_info_hours')}</p>
                                        <p>{t('booking.form.disclaimer_info_start_date')}</p>

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
                                                <DatePickerField name="dateFrom" changeCallback={() => {
                                                    setFieldValue("boxes", [], true)
                                                    resetTable()
                                                }}/>
                                                {errors && touched && errors.dateFrom && (
                                                    <div style={{color: "red"}}>{errors.dateFrom}</div>
                                                )}
                                            </div>

                                            <div className="col-md-12">
                                                <label htmlFor="dateTo">{t('booking.form.date_to')}</label>
                                                <DatePickerField name="dateTo" changeCallback={() => {
                                                    console.log(touched)
                                                    setFieldValue("boxes", [], true)
                                                    resetTable()
                                                }}/>
                                                {errors && touched && errors.dateTo && (
                                                    <div style={{color: "red"}}>{errors.dateTo}</div>
                                                )}
                                            </div>

                                            <div className="col-12 my-3">
                                                <button
                                                    disabled={!(touched.dateFrom || touched.dateTo) || (errors.dateFrom || errors.dateTo)}
                                                    className="btn btn-primary"
                                                    style={{backgroundColor: "#7749F8"}}
                                                    onClick={() => {
                                                        console.log(isValid)
                                                        console.log(errors)
                                                        fetchBoxes(values.hotelId, values.dateFrom, values.dateTo)
                                                    }}
                                                    type="button"
                                                >
                                                    {t("booking.search_boxes")}
                                                </button>
                                            </div>

                                            {showTable &&

                                            <div className="col-12">
                                                <DataTableField
                                                    name="boxes"
                                                    className={"rounded-0"}
                                                    noDataComponent={i18n.t("booking.no_boxes_available")}
                                                    columns={columns}
                                                    noHeader={true}
                                                    data={boxes}
                                                    selectableRows
                                                    theme={themeColor}
                                                    clearSelectedRows={clearRows}
                                                />
                                                {errors && touched && errors.boxes && touched.boxes && (
                                                    <div style={{color: "red"}}>{errors.boxes}</div>
                                                )}
                                            </div>
                                            }

                                            <div className="col-12 d-flex justify-content-center my-3">
                                                <button
                                                    disabled={!isValid}
                                                    className="btn btn-primary"
                                                    style={{backgroundColor: "#7749F8"}}
                                                    type="submit"
                                                >
                                                    {t("booking.create_reservation")}
                                                </button>
                                            </div>
                                        </Form>
                                    </div>
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
