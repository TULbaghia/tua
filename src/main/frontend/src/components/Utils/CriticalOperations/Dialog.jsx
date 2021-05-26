import React, {useState} from 'react';
import {Button, Modal} from "react-bootstrap";
import i18n from '../../../i18n';

function Dialog(props) {
    const [show, setShow] = useState(true);

    const handleClose = () => {
        setShow(false);
        setTimeout(() => {
            props.dispatch({
                type: "REMOVE_DIALOG",
                id: props.id
            })
        }, 100);
    };

    const handleCloseCancel = () => {
        handleClose();
        props.callbackOnCancel();
    }

    const handleCloseSave = () => {
        handleClose();
        props.callbackOnSave();
    }

    return (
        <Modal show={show} onHide={handleCloseCancel} className={"text-dark"}>
            <Modal.Header closeButton>
                <Modal.Title>{props.title}</Modal.Title>
            </Modal.Header>
            <Modal.Body style={{whiteSpace: "pre-line"}}>{props.message}</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleCloseCancel}>
                    {i18n.t("dialog.button.cancel")}
                </Button>
                <Button variant="primary" onClick={handleCloseSave}>
                    {i18n.t(props.textButtonSave ?? "dialog.button.save")}
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default Dialog;