import React from "react";
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

export default function RatingComponent({id, rate, login, content, date, hidden}) {

    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    // const visible = 'RatingVisibility.VISIBLE';
    // const unvisible = 'RatingVisibility.HIDDEN';

    const handleChangeVisibilityClick = (id) => {
        axios.patch(`${process.env.REACT_APP_API_BASE_URL}/resources/ratings/changeVisibility/${id}`, {}, {
            headers: {
                "Authorization": `${localStorage.getItem("token")}`
            }
        })
        // api.changeVisibility(id, {headers: {Authorization: localStorage.getItem("token")}})
            .then(() => {
                dispatchNotificationSuccess({message: 18n.t('change.comment.visibility.success')})
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
                    /><Button className="btn-sm float-right mr-2"
                              onClick={() => handleChangeVisibilityClick(id)}>
                    {hidden ? i18n.t('show.button') : i18n.t('hide.button')}
                    </Button>
                    <FontAwesomeIcon className={"float-right mr-2"} icon={faEllipsisV} size={"lg"}/></Card.Header>
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