import React, {useEffect, useState} from "react";
import {Rating} from "@material-ui/lab";
import StarBorderIcon from '@material-ui/icons/StarBorder';
import {Button, Card} from "react-bootstrap";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {faEdit, faTrash} from '@fortawesome/free-solid-svg-icons'
import i18n, {dateConverter} from '../../i18n';
import {api} from "../../Api";
import {useLocale} from "../LoginContext";
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";
import {useNotificationDangerAndInfinity, useNotificationSuccessAndShort} from "../Utils/Notification/NotificationProvider";
import {useDialogPermanentChange} from "../Utils/CriticalOperations/CriticalOperationProvider";
import axios from "axios";

export default function RatingComponent({id, rate, login, content, date, modificationDate, hidden, triggerRefresh}) {
    const dangerNotifier = useNotificationDangerAndInfinity();
    const successNotifier = useNotificationSuccessAndShort();
    const confirmDialog = useDialogPermanentChange();
    const {token, username, currentRole} = useLocale();
    const [etag, setEtag] = useState();
    const [hiddenValue, setHiddenValue] = useState()
    const [editMode, setEditMode] = useState(false)
    const [newRate, setNewRate] = useState(rate)
    const [newContent, setNewContent] = useState(content)

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
        api.deleteRating(id, {
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

    const updateComment = (evt) => {
        let x = {id: id, rate: newRate}
        if (newContent !== '') {
            x.comment = newContent;
        }
        api.updateRating(x, {
            headers: {
                Authorization: token,
                "If-Match": etag
            }
        }).then((res) => {
            successNotifier({
                message: i18n.t("comment.success.update")
            })
            triggerRefresh()
        }).catch((e) => ResponseErrorHandler(e, dangerNotifier));
    }

    const updateDialog = (evt) => {
        confirmDialog({
            callbackOnSave: (evt) => updateComment(evt)
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
                triggerRefresh();
            })
            .catch(err => {
                ResponseErrorHandler(err, dangerNotifier)
            })
    }

    const handleChangeContent = (event) => {
        setNewContent(event.target.value);
    }

    return (
        <>
            <Card className="mb-4">
                <Card.Header className={"d-flex justify-content-between text-left p-1 align-items-center"}>
                    <Rating className={"star"}
                            size={"large"}
                            defaultValue={rate}
                            readOnly={!editMode}
                            precision={1}
                            emptyIcon={editMode ? <StarBorderIcon fontSize="inherit"/> : ''}
                            onChange={(event, newValue) => {
                                setNewRate(newValue);
                            }}
                    />
                    <div>
                        {currentRole === 'ADMIN' ?
                            <Button className="btn-sm float-right mr-2" style={{backgroundColor: "#7749F8"}}
                                    onClick={() => handleChangeVisibilityClick(id)}>
                                {hiddenValue ? i18n.t('show.button') : i18n.t('hide.button')}
                            </Button>
                            : null
                        }
                        {username === login ?
                            <FontAwesomeIcon onClick={() => {
                                setEditMode(!editMode)
                            }} className={"mr-2"} style={{cursor: "pointer"}} icon={faEdit} size={"1x"}/> : ''}
                        {etag && username === login ?
                            <FontAwesomeIcon onClick={deleteDialog} className={"mr-2"} style={{cursor: "pointer"}} icon={faTrash}
                                             size={"1x"}/> : ''}
                    </div>
                </Card.Header>
                <Card.Body>
                    <Card.Text className={"text-justify"}>
                        {hiddenValue ? i18n.t('hiddenComment') : ''}
                        {editMode && !hiddenValue ?
                            <div><textarea className={"w-100"} onChange={handleChangeContent} defaultValue={content}/></div> : ''}
                        {!editMode && !hiddenValue ? content : ''}
                    </Card.Text>
                </Card.Body>
                <Card.Footer className={"p-2"}>{editMode ? <span className={"float-left"}><Button onClick={updateDialog}
                                                                                                  className="btn-sm">{i18n.t('rate.update')}</Button></span> : ''}
                    <span className={"float-right text-muted"}>{!modificationDate ? date : dateConverter(modificationDate.slice(0, -5))}</span><span
                        className={"float-right mr-2"}>{login}</span></Card.Footer>
            </Card>
        </>
    )
}