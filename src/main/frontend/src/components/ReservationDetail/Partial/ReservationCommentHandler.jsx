import React, {useEffect, useState} from 'react';
import {withNamespaces} from "react-i18next";
import {Card, Col, Row} from "react-bootstrap";
import {useLocale} from "../../LoginContext";
import RatingComponent from "../../HotelInfo/RatingComponent";
import NewRatingComponent from "../../HotelInfo/NewRatingComponent";
import i18n from "i18next";
import {apiGetRating} from "../ReservationDetailApiUtil";
import {ResponseErrorHandler} from "../../Validation/ResponseErrorHandler";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../../Utils/Notification/NotificationProvider";
import {dateConverter} from "../../../i18n";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faComment, faCreditCard} from "@fortawesome/free-solid-svg-icons";

function ReservationCommentHandler({reservation, refreshComponent, ...props}) {
    const {token, currentRole} = useLocale();
    const dispatchDanger = useNotificationDangerAndInfinity();
    const dispatchSuccess = useNotificationSuccessAndShort();
    const [rating, setRating] = useState({});

    const refreshRating = (showInfo = true) => {
        if (reservation.ratingId != null) {
            apiGetRating({
                id: reservation.ratingId,
                token
            }).then(r => {
                setRating({...r.data});
                if (showInfo) {
                    dispatchSuccess({
                        message: i18n.t("bookingDetails.rating.refresh")
                    })
                }
            }).catch(err => ResponseErrorHandler(err, dispatchDanger));
        }
    }

    useEffect(() => {
        refreshRating(false);
    }, [reservation.ratingId]);

    return (
        <Row className={"mt-4"}>
            <Col xs={12}>
                {reservation.ratingId != null && rating.id &&
                <div className={"mb-n3"}>
                    <RatingComponent id={rating.id} rate={rating.rate} hidden={rating.hidden}
                                     login={rating.createdBy} content={rating.comment}
                                     date={dateConverter(rating.creationDate.slice(0, -5))}
                                     modificationDate={rating.modificationDate}
                                     triggerRefresh={refreshComponent}/>
                </div>
                }
                {reservation.ratingId == null &&
                    <Card className={"mb-1"}>
                        <Card.Header>
                            <FontAwesomeIcon icon={faComment} size={"1x"} className={"mr-2"}/>
                            <span>{i18n.t("bookingDetails.header.comment")}</span>
                        </Card.Header>
                        <Card.Body>
                            <NewRatingComponent triggerRefresh={refreshComponent}
                                                placeholder={i18n.t('add.new.comment')}
                                                header={i18n.t('add.new.rating')}
                                                buttonText={i18n.t('add.rating')}
                                                bookings={[reservation]}/>
                        </Card.Body>
                    </Card>
                }
            </Col>
        </Row>
    );
}

export default withNamespaces()(ReservationCommentHandler);