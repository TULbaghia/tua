import React, {useState} from 'react';
import {Alert, ProgressBar} from "react-bootstrap";

export const dialogType = {
    DANGER: "danger",
    WARNING: "warning",
    SUCCESS: "success",
    INFO: "info",
    PRIMARY: "primary",
    SECONDARY: "secondary",
    LIGHT: "light",
}

export const dialogDuration = {
    MINUTE: 60_000,
    LONG: 10_000,
    SHORT: 5_000,
    SECOND: 1_000,
}

const RefreshNotification = (props) => {
    const [width, setWidth] = useState(0);
    const [intervalId, setIntervalId] = useState(null);
    const [useEffectStart, setUseEffectStart] = useState(false);

    const step = 1;

    const handleStartTimer = () => {
        const id = setInterval(() => {
            setWidth(prev => {
                if (prev < 100) {
                    return prev + step;
                }

                clearInterval(id);
                return prev;
            });
        }, (props.dialogDuration + 500) / (100 / step));
        setIntervalId(id);
    };

    const handlePauseTimer = () => {
        clearInterval(intervalId);
    };

    const handleCloseNotification = () => {
        handlePauseTimer();
        setTimeout(() => {
            props.dispatch({
                type: "REMOVE_NOTIFICATION",
                id: props.id
            })
        }, 100);
    };

    React.useEffect(() => {
        if (width >= 100) {
            handleCloseNotification()
        }
    }, [width])

    React.useEffect(() => {
        handleStartTimer();
        setUseEffectStart(true);
    }, []);

    return (
        <Alert key={props.id} variant={props.dialogType} dismissible className={"text-left"}
               onClose={handleCloseNotification}
               onClick={handleCloseNotification}>
            {props.title ? <Alert.Heading as={"h6"} className={"font-weight-bold"}>{props.title}</Alert.Heading> : ""}
            <div style={{whiteSpace: "pre-line"}}>{props.message}</div>
            <ProgressBar now={width} className={"alert-progress-bar"}/>
        </Alert>
    );
};

export default RefreshNotification;