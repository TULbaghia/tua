import React, {useEffect, useState} from "react";
import {Rating} from "@material-ui/lab";
import {Button, Card} from "react-bootstrap";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faEllipsisV} from '@fortawesome/free-solid-svg-icons'
import i18n from '../../i18n';
import {api} from "../../Api";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import axios from "axios";
import {useLocale} from "../LoginContext";

export default function RatingComponent({id, rate, login, content, date, hidden}) {

    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const [etag, setETag] = useState()
    const {token, currentRole} = useLocale()
    const [hiddenValue, setHiddenValue] = useState()

    useEffect(() => {
        setHiddenValue(hidden)
        axios.get(`${process.env.REACT_APP_API_BASE_URL}/resources/ratings/rating/${id}`, {
            headers: {
                "Authorization": token,
            }
        })
            .then(res => {
                setETag(res.headers.etag)
            })
    })

    const handleChangeVisibilityClick = (id) => {
        axios.patch(`${process.env.REACT_APP_API_BASE_URL}/resources/ratings/changeVisibility/${id}`, {}, {
            headers: {
                "Authorization": token,
                "If-Match": etag
            }
        })
            .then(() => {
                setHiddenValue(!hiddenValue)
                dispatchNotificationSuccess({message: i18n.t('change.comment.visibility.success')})
            })
            .catch(err => {
                ResponseErrorHandler(err, dispatchNotificationDanger)
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
                    {currentRole === 'ADMIN' ?
                        <Button className="btn-sm float-right mr-2"
                                onClick={() => handleChangeVisibilityClick(id)}>
                            {hiddenValue ? i18n.t('show.button') : i18n.t('hide.button')}
                        </Button>
                        : null
                    }
                    <FontAwesomeIcon className={"float-right mr-2"} icon={faEllipsisV} size={"lg"}/></Card.Header>
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