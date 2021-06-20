import React, {useEffect, useState} from 'react';
import {useHistory, useParams} from "react-router";
import NotFound from "../ErrorPages/NotFound";
import {getBox, getHotel, getRenter, getReservation} from "./ReservationDetailApiUtil";
import {useLocale} from "../LoginContext";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {Badge, Button, Card, Col, Container, Row, Table} from "react-bootstrap";
import i18n from "i18next";
import {v4} from "uuid";
import BreadCrumb from "../Partial/BreadCrumb";
import {Link} from "react-router-dom";
import queryString from "query-string";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCreditCard, faHotel, faUser} from "@fortawesome/free-solid-svg-icons";
import {dateConverter} from "../../i18n";
import {withNamespaces} from "react-i18next";
import './ReservationDetailWrapper.scss';
import ReservationStateHandler from "./Partial/ReservationStateHandler";
import {rolesConstant} from "../../Constants";
import ReservationCommentHandler from "./Partial/ReservationCommentHandler";

function ReservationDetail(props) {
    const {id} = useParams();
    const history = useHistory();
    const {token, currentRole} = useLocale();
    const dispatchError = useNotificationDangerAndInfinity();
    const dispatchSuccess = useNotificationSuccessAndShort();

    const [hotel, setHotel] = useState({});
    const [reservation, setReservation] = useState({
        bookingLine: [],
        bookingStatus: "",
        creationDate: "",
        dateFrom: "",
        dateTo: ""
    })
    const [renter, setRenter] = useState({})

    const fetchData = async (showInfo = false, reservationRef = true, hotelRef = true, renterRef = true) => {
        if (reservationRef) setReservation({
            bookingLine: [],
            bookingStatus: "",
            creationDate: "",
            dateFrom: "",
            dateTo: ""
        });
        if (hotelRef) setHotel({})
        if (renterRef) setRenter({});
        try {
            let reservationE = reservation;
            if (reservationRef) {
                const responseReservation = await getReservation({id, token});

                const durationFrom = new Date(responseReservation.data.dateFrom.slice(0, -5));
                const durationTo = new Date(responseReservation.data.dateTo.slice(0, -5));
                const differenceInTime = durationTo.getTime() - durationFrom.getTime();
                const differenceInDays = Math.ceil(differenceInTime / (1000 * 3600 * 24));

                reservationE = {
                    ...responseReservation.data,
                    ETag: responseReservation.headers.etag,
                    differenceInDays
                };

                for (const x of reservationE.bookingLine) {
                    let response = await getBox({id: x.boxId, token});
                    x.box = {...response.data, ETag: response.headers.etag};
                }

                setReservation(reservationE)
            }

            if (renterRef) {
                getRenter({login: reservationE.renterLogin, token, currentRole}).then(res => {
                    setRenter({...res.data, ETag: res.headers.etag})
                }).catch(err => ResponseErrorHandler(err, dispatchError));
            }

            if (hotelRef) {
                const hotelId = reservationE.bookingLine.map(x => x.box.hotelId)
                if (hotelId.length > 0) {
                    getHotel({id: hotelId[0], token}).then(res => {
                        setHotel({...res.data, ETag: res.headers.etag});
                    }).catch(err => ResponseErrorHandler(err, dispatchError));
                }
            }

            if (showInfo) {
                dispatchSuccess({
                    message: i18n.t("bookingDetails.refresh"),
                });
            }

        } catch (err) {
            ResponseErrorHandler(err, dispatchError);
        }
    }

    useEffect(fetchData, [token]);

    const prevHistoryHandle = () => {
        const ref = queryString.parse(history.location.search).ref;
        if (ref === "archive") {
            return (
                <li className="breadcrumb-item"><Link to="/archiveReservations">{i18n.t("archiveReservations")}</Link>
                </li>);
        } else if (ref === "active") {
            return (<li className="breadcrumb-item"><Link to="/activeReservations">{i18n.t("activeReservations")}</Link>
            </li>);
        }
        return (<></>);
    }

    const bookingLinePrinter = (bookingLine) => {
        return (
            <tr key={v4()}>
                <td>{bookingLine.id}</td>
                <td>{i18n.t(bookingLine.box ? bookingLine.box.animalType : "")}</td>
                <td>{Math.round(bookingLine.pricePerDay * 100) / 100} {i18n.t("currency")}</td>
                <td>{Math.round(bookingLine.pricePerDay * reservation.differenceInDays * 100) / 100} {i18n.t("currency")}</td>
            </tr>
        )
    }

    if (!isFinite(id)) {
        return <NotFound/>;
    }

    return (
        <Container fluid className={"pb-5 reservationDetail"}>

            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{i18n.t('mainPage')}</Link></li>
                {prevHistoryHandle()}
                <li className="breadcrumb-item active" aria-current="page">{i18n.t('bookingDetails.nav')}</li>
            </BreadCrumb>

            <Container className={"mt-3 mb-2 floating-no-absolute px-5 py-4 darkify"}>
                <Row>
                    <Col xs={12}
                         className={"d-flex flex-wrap flex-lg-nowrap justify-content-center justify-content-md-between"}>
                        <h2 className={"text-center"}>{i18n.t("bookingDetails.header.title")}{id}</h2>
                        <ReservationStateHandler reservation={reservation} refreshComponent={fetchData}/>
                    </Col>
                </Row>
                { reservation.bookingStatus === "FINISHED" &&
                <ReservationCommentHandler reservation={reservation} refreshComponent={() => fetchData(true)}/>
                }
                <Row className={"d-flex"}>
                    <Col md={6} className={"d-flex mt-4"}>
                        <Card className={"flex-grow-1"}>
                            <Card.Header>
                                <FontAwesomeIcon icon={faCreditCard} size={"1x"} className={"mr-2"}/>
                                <span>{i18n.t("bookingDetails.header.reservation")}</span>
                            </Card.Header>
                            <Card.Body>
                                <div>
                                    <div
                                        className={"font-weight-bold"}>{i18n.t("bookingDetails.reservation.bookingNumber")}</div>
                                    <div className={"ml-2"}><Badge variant={"info"}>#{reservation.id}</Badge></div>
                                </div>
                                <div>
                                    <div
                                        className={"font-weight-bold"}>{i18n.t("bookingDetails.reservation.bookingStatus")}</div>
                                    <div className={"ml-2"}><Badge
                                        variant={"primary"}>{i18n.t(reservation.bookingStatus.toLowerCase() + "BookingStatus")}</Badge>
                                    </div>
                                </div>
                                <div>
                                    <div
                                        className={"font-weight-bold"}>{i18n.t("bookingDetails.reservation.creationDate")}</div>
                                    <div className={"ml-2"}>{dateConverter(reservation.creationDate.slice(0, -5))}</div>
                                </div>
                                <div>
                                    <div
                                        className={"font-weight-bold"}>{i18n.t("bookingDetails.reservation.dateFrom")}</div>
                                    <div className={"ml-2"}>{dateConverter(reservation.dateFrom.slice(0, -5))}</div>
                                </div>
                                <div>
                                    <div
                                        className={"font-weight-bold"}>{i18n.t("bookingDetails.reservation.dateTo")}</div>
                                    <div className={"ml-2"}>{dateConverter(reservation.dateTo.slice(0, -5))}</div>
                                </div>
                                <div>
                                    <div
                                        className={"font-weight-bold"}>{i18n.t("bookingDetails.reservation.price")}</div>
                                    <div className={"ml-2"}><Badge
                                        variant={"info"}>{reservation.price} {i18n.t("currency")}</Badge></div>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col md={6} className={"d-flex mt-4"}>
                        <Card className={"flex-grow-1"}>
                            <Card.Header>
                                <FontAwesomeIcon icon={faUser} size={"1x"} className={"mr-2"}/>
                                <span>{i18n.t("bookingDetails.header.renter")}</span>
                            </Card.Header>
                            <Card.Body>
                                <div>
                                    <div className={"font-weight-bold"}>{i18n.t("bookingDetails.renter.login")}</div>
                                    <div className={"ml-3"}>{renter.login}</div>
                                </div>
                                <div>
                                    <div className={"font-weight-bold"}>{i18n.t("bookingDetails.renter.personal")}</div>
                                    <div className={"ml-3"}>{renter.firstname} {renter.lastname}</div>
                                </div>
                                <div>
                                    <div className={"font-weight-bold"}>{i18n.t("bookingDetails.renter.email")}</div>
                                    <div className={"ml-3"}>{renter.email}</div>
                                </div>
                                <div>
                                    <div
                                        className={"font-weight-bold"}>{i18n.t("bookingDetails.renter.contactNumber")}</div>
                                    <div className={"ml-3"}>{renter.contactNumber}</div>
                                </div>
                                <div style={{display: (currentRole === rolesConstant.manager ? "block" : "none")}}>
                                    <div className={"font-weight-bold"}>{i18n.t("bookingDetails.renter.enabled")}</div>
                                    <div
                                        className={"ml-3"}>{i18n.t("bookingDetails.renter.enabled." + (renter.enabled ? "yes" : "no"))}</div>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
                <Row xs={12}>
                    <Col md={12} className={"d-flex mt-4 pt-md-1"}>
                        <Card className={"flex-grow-1"}>
                            <Card.Header>
                                <FontAwesomeIcon icon={faHotel} size={"1x"} className={"mr-2"}/>
                                <span>{i18n.t("bookingDetails.header.reservationDetail")}</span>
                            </Card.Header>
                            <Card.Body>
                                <Row>
                                    <Col md={12} className={"d-flex flex-column-reverse flex-md-row px-0"}>
                                        <Col md={6}>
                                            <div>
                                                <div
                                                    className={"font-weight-bold"}>{i18n.t("bookingDetails.hotel.name")}</div>
                                                <div className={"ml-2"}>{hotel.name}</div>
                                            </div>
                                            <div>
                                                <div
                                                    className={"font-weight-bold"}>{i18n.t("bookingDetails.hotel.address")}</div>
                                                <div className={"ml-2"}>{hotel.address}</div>
                                            </div>
                                            <div>
                                                <div
                                                    className={"font-weight-bold"}>{i18n.t("bookingDetails.hotel.cityName")}</div>
                                                <div className={"ml-2"}>{hotel.cityName}</div>
                                            </div>
                                        </Col>
                                        <Col md={6}>
                                            <div className={"pl-0 pl-md-3 ml-md-1"}>
                                                <div
                                                    className={"font-weight-bold"}>{i18n.t("bookingDetails.hotel.image")}</div>
                                                <img className={"img-fluid"} src={hotel.image} alt={"Hotel"}/>
                                            </div>
                                        </Col>
                                    </Col>
                                    <Col md={12} className={"d-flex flex-column-reverse flex-md-row"}>
                                        <div id={"reservationDetailBoxTable"} className={"table-responsive"}>
                                            <hr className={"w-75 ml-0 my-4"}/>
                                            <h5 className={"font-weight-bold"}>{i18n.t("bookingDetails.reservation.boxList")}</h5>
                                            <Table className={"table table-striped table-hover"}>
                                                <thead>
                                                <tr>
                                                    <th>{i18n.t("bookingDetails.reservation.table.id")}</th>
                                                    <th>{i18n.t("bookingDetails.reservation.table.type")}</th>
                                                    <th>{i18n.t("bookingDetails.reservation.table.pricePerDay")}</th>
                                                    <th>{i18n.t("bookingDetails.reservation.table.pricePerLine")}</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                {reservation.bookingLine.map(bookingLinePrinter)}
                                                <tr>
                                                    <td colSpan={3} className={"text-right font-weight-bold"}>
                                                        {i18n.t("bookingDetails.reservation.table.priceTotal")}
                                                    </td>
                                                    <td className={"font-weight-bold"}>{reservation.price} {i18n.t("currency")}</td>
                                                </tr>
                                                </tbody>
                                            </Table>
                                        </div>
                                    </Col>
                                </Row>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </Container>
    );
}

export default withNamespaces()(ReservationDetail);