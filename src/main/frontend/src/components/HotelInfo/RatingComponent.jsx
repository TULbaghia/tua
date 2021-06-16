import React, {useEffect, useState} from "react";
import {Rating} from "@material-ui/lab";
import {Card} from "react-bootstrap";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faEllipsisV, faTrash} from '@fortawesome/free-solid-svg-icons'
import i18n from '../../i18n';
import {api} from "../../Api";
import {useLocale} from "../LoginContext";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";


export default function RatingComponent({id, rate, login, content, date, hidden, triggerRefresh}) {
    const dangerNotifier = useNotificationDangerAndInfinity();
    const successNotifier = useNotificationSuccessAndShort();
    const confirmDialog = useDialogPermanentChange();
    const {token, username} = useLocale();
    const [etag, setEtag] = useState();
    window.api = api;

    useEffect(() => {
        if (username === login) {
            api.getHotelRating(id, {
                headers: {
                    Authorization: token,
                }
            }).then((res) => {
                setEtag(res.headers.etag);
            }).catch((e) => ResponseErrorHandler(e, dangerNotifier));
        }
    }, [token]);

    const deleteComment = (evt) => {
        api.deleteRating(id,{
            headers: {
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            successNotifier({
                message: i18n.t("comment.success.delete")
            })
            triggerRefresh()
        }).catch((e) => ResponseErrorHandler(e, dangerNotifier));
    }
    const deleteDialog = (evt) => {
        confirmDialog({
            callbackOnSave: (evt) => deleteComment(evt)
        })
    }

    return (
        <>
            <Card className="mb-4">
                <Card.Header className={"d-flex justify-content-between text-left p-1 align-items-center"}>
                    <Rating
                        size={"large"}
                        value={rate}
                        readOnly
                        precision={0.5}
                    />
                    <div>
                        <FontAwesomeIcon className={"mr-3"} icon={faEllipsisV} size={"1x"}/>
                        { etag ? <FontAwesomeIcon onClick={deleteDialog} className={"mr-2"} style={{cursor: "pointer"}} icon={faTrash} size={"1x"}/> : '' }
                    </div>
                    </Card.Header>
                <Card.Body>
                    <Card.Text className={"text-justify"}>
                        {hidden ? i18n.t('hiddenComment') : content}
                    </Card.Text>
                </Card.Body>
                <Card.Footer className={"p-2"}><span className={"float-right text-muted"}>{date}</span><span
                    className={"float-right mr-2"}>{login}</span></Card.Footer>
            </Card>
        </>
    )
}