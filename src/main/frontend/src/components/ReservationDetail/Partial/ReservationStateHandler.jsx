import React from 'react';
import {withNamespaces} from "react-i18next";
import {Button} from "react-bootstrap";
import i18n from "i18next";
import {useDialogPermanentChange} from "../../Utils/CriticalOperations/CriticalOperationProvider";
import {useLocale} from "../../LoginContext";
import {apiCancelBooking, apiEndBooking, apiInProgressBooking} from "../ReservationDetailApiUtil";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../../Utils/Notification/NotificationProvider";
import {rolesConstant} from "../../../Constants";
import {ResponseErrorHandler} from "../../Validation/ResponseErrorHandler";
import moment from "moment";

const stateEnums = {CANCELLED: "CANCELLED", PENDING: "PENDING", IN_PROGRESS: "IN_PROGRESS", FINISHED: "FINISHED"}

const allowedStateTransition = (currentState) => {
    if (currentState === stateEnums.CANCELLED) {
        return [];
    } else if (currentState === stateEnums.PENDING) {
        return [stateEnums.IN_PROGRESS, stateEnums.CANCELLED];
    } else if (currentState === stateEnums.IN_PROGRESS) {
        return [stateEnums.FINISHED];
    } else if (currentState === stateEnums.FINISHED) {
        return [];
    } else {
        return [];
    }
}

const verifyStateTransition = (currentState, destinationState) => {
    return allowedStateTransition(currentState).includes(destinationState)
}


function ReservationStateHandler({reservation, refreshComponent, ...props}) {
    const dispatchDanger = useNotificationDangerAndInfinity();
    const dispatchSuccess = useNotificationSuccessAndShort();
    const dispatchDialog = useDialogPermanentChange();
    const {token, currentRole} = useLocale();

    const cancelBooking = () => {
        apiCancelBooking({
            id: reservation.id,
            token: token,
            ETag: reservation.ETag,
            dispatchDanger
        }).then(r => {
            dispatchSuccess({message: i18n.t("bookingDetails.success.cancel")});
            refreshComponent(true);
        }).catch(err => ResponseErrorHandler(err, dispatchDanger));
    }

    const inProgressBooking = () => {
        apiInProgressBooking({
            id: reservation.id,
            token: token,
            ETag: reservation.ETag,
            dispatchDanger
        }).then(r => {
            dispatchSuccess({message: i18n.t("bookingDetails.success.in_progress")});
            refreshComponent(true);
        }).catch(err => ResponseErrorHandler(err, dispatchDanger));
    }

    const endBooking = () => {
        apiEndBooking({
            id: reservation.id,
            token: token,
            ETag: reservation.ETag,
            dispatchDanger
        }).then(r => {
            dispatchSuccess({message: i18n.t("bookingDetails.success.finish")});
            refreshComponent(true);
        }).catch(err => ResponseErrorHandler(err, dispatchDanger));
    }

    const promptAct = (bookingAction) => {
        dispatchDialog({
            callbackOnSave: () => bookingAction()
        })
    }

    const allowToCancel = () => {
        if (reservation.creationDate) {
            const createDate = moment(reservation.creationDate.slice(0, -5)).unix();
            const startDate = moment(reservation.dateFrom.slice(0, -5)).unix();
            const now = moment().unix();
            const day = 24 * 60 * 60;

            return now - createDate < day && startDate - now > 2 * day
        }
    }

    return (
        <div className={"d-flex"}>
            <div className={"bookingStates d-flex"}>
                {verifyStateTransition(reservation.bookingStatus, stateEnums.CANCELLED) && allowToCancel() &&
                <Button className={"mr-2 bg-danger"} onClick={() => promptAct(cancelBooking)}>
                    {i18n.t("bookingDetails.buttons.cancel")}
                </Button>
                }
                <div style={{display: (currentRole === rolesConstant.manager ? "flex" : "none")}}>
                    {verifyStateTransition(reservation.bookingStatus, stateEnums.IN_PROGRESS) &&
                    <Button className={"mr-2 bg-success"} onClick={() => promptAct(inProgressBooking)}>
                        {i18n.t("bookingDetails.buttons.in_progress")}
                    </Button>
                    }
                    {verifyStateTransition(reservation.bookingStatus, stateEnums.FINISHED) &&
                    <Button className={"mr-2 bg-success"} onClick={() => promptAct(endBooking)}>
                        {i18n.t("bookingDetails.buttons.end_booking")}
                    </Button>
                    }
                </div>
            </div>
            <Button onClick={() => refreshComponent(true)}>{i18n.t("refresh")}</Button>
        </div>
    );
}

export default withNamespaces()(ReservationStateHandler);