import React, {useEffect, useState} from "react";
import {Rating} from "@material-ui/lab";
import {Button, Card} from "react-bootstrap";
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
import axios from "axios";

export default function RatingComponent({id, rate, login, content, date, hidden, triggerRefresh}) {
    const dangerNotifier = useNotificationDangerAndInfinity();
    const successNotifier = useNotificationSuccessAndShort();
    const confirmDialog = useDialogPermanentChange();
    const {token, username, currentRole} = useLocale();
    const [etag, setEtag] = useState();
    const [hiddenValue, setHiddenValue] = useState()

    useEffect(() => {
        setHiddenValue(hidden)
        if (username === login || currentRole === 'ADMIN') {
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

    const handleChangeVisibilityClick = (id) => {
        axios.patch(`${process.env.REACT_APP_API_BASE_URL}/resources/ratings/changeVisibility/${id}`, {}, {
            headers: {
                "Authorization": token,
                "If-Match": etag
            }
        })
            .then(() => {
                setHiddenValue(!hiddenValue)
                successNotifier({message: i18n.t('change.comment.visibility.success')})
            })
            .catch(err => {
                ResponseErrorHandler(err, dangerNotifier)
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
                        {currentRole === 'ADMIN' ?
                            <Button className="btn-sm float-right mr-2"
                                    onClick={() => handleChangeVisibilityClick(id)}>
                                {hiddenValue ? i18n.t('show.button') : i18n.t('hide.button')}
                            </Button>
                            : null
                        }
                        <FontAwesomeIcon className={"mr-3"} icon={faEllipsisV} size={"1x"}/>
                        { etag && username === login ? <FontAwesomeIcon onClick={deleteDialog} className={"mr-2"} style={{cursor: "pointer"}} icon={faTrash} size={"1x"}/> : '' }
                    </div>
                    </Card.Header>
                <Card.Body>
                    <Card.Text className={"text-justify"}>
                        {hiddenValue ? i18n.t('hiddenComment') : content}
                    </Card.Text>
                </Card.Body>
                <Card.Footer className={"p-2"}><span className={"float-right text-muted"}>{date}</span><span
                    className={"float-right mr-2"}>{login}</span></Card.Footer>
            </Card>
        </>
    )
}