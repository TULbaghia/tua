import React, {useState} from "react";
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort,
    useNotificationWarningAndLong
} from "../Utils/Notification/NotificationProvider";
import {api} from "../../Api";
import i18n from "../../i18n";
import {withNamespaces} from "react-i18next";
import 'react-datepicker/dist/react-datepicker.css';
import './ReportGeneratorFormWrapper.scss';
import {useLocale} from "../LoginContext";
import moment from 'moment';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import {Col, Container, Row} from "react-bootstrap";
import DatePickerCustom from "./DatePickerCustom";
import ReportsTable from "./ReportsTable";

function ReportGeneratorForm() {
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    const [isFetching, setIsFetching] = useState(false);
    const {token} = useLocale();

    const bookingsScheme = [
        {
            id: "",
            dateFrom: "",
            dateTo: "",
            modificationDate: "",
            status: "",
            price: "",
            rating: null,
            ownerLogin: "",
        }
    ];

    const [bookings, setBookings] = useState(bookingsScheme);

    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationWarning = useNotificationWarningAndLong()

    const handleGenerateReport = e => {
        setIsFetching(true);
        e.preventDefault()
        api.generateReport(moment(startDate).unix(), moment(endDate).unix(), {
            headers: {
                Authorization: token,
            }
        }).then(res => {
            setBookings(res.data.bookings);
            if (res.data.count !== 0) {
                dispatchNotificationSuccess({message: i18n.t('reportGenerator.success')})
            } else {
                setBookings(bookingsScheme);
                dispatchNotificationWarning({message: i18n.t('reportGenerator.no_results')})
            }
            setIsFetching(false);
        }).catch(err => {
            ResponseErrorHandler(err, dispatchNotificationDanger);
            setIsFetching(false)
        });
    }

    return (
        <div id="report-container" className={"mb-2 container-fluid"}>
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{i18n.t('mainPage')}</Link></li>
                <li className="breadcrumb-item"><Link to="/">{i18n.t('managerDashboard')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{i18n.t('reportGenerator.title')}</li>
            </BreadCrumb>
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div>
                            <Row className="text-center">
                                <Col className="mb-sm-3">
                                    <h1>{i18n.t('reportGenerator.title')}</h1>
                                </Col>
                            </Row>
                            <Row className="text-center">
                                <Col className="d-flex justify-content-center">
                                    <DatePickerCustom setPickDate={setStartDate}
                                                      pickDate={startDate}
                                                      setEndDate={setEndDate}
                                                      currentEndDate={endDate}
                                                      label={i18n.t('reportGenerator.from')}/>
                                    <DatePickerCustom setPickDate={setEndDate}
                                                      pickDate={endDate}
                                                      minDate={startDate}
                                                      label={i18n.t('reportGenerator.to')}/>
                                </Col>
                            </Row>
                            <Row className="justify-content-center">
                                <Col sm={6} className="my-4">
                                    <button className="btn btn-lg btn-primary btn-block btn-background-custom"
                                            type="submit"
                                            onClick={handleGenerateReport}
                                            disabled={isFetching || !startDate || !endDate}>
                                        {i18n.t('reportGenerator.action')}
                                        {isFetching &&
                                        <div className="ml-4 align-content-center spinner-border" role="status"/>}
                                    </button>
                                </Col>
                            </Row>
                            <Row>
                                <Col className="d-inline-block my-3">
                                    <ReportsTable bookings={bookings}/>
                                </Col>
                            </Row>
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default withNamespaces()(ReportGeneratorForm);
