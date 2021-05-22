import React, {createContext, useContext, useReducer} from 'react';
import Notification, {dialogDuration, dialogType} from "./Notification";
import {v4} from "uuid";
import './NotificationWrapper.scss'
import i18n from '../../i18n';

const NotificationContext = createContext();

const NotificationProvider = (props) => {
    const [state, dispatch] = useReducer((state, action) => {
        switch (action.type) {
            case "ADD_NOTIFICATION":
                return [...state, {...action.payload}];
            case "REMOVE_NOTIFICATION":
                return state.filter(e => e.id !== action.id);
            default:
                return state;
        }
    }, []);

    return (
        <NotificationContext.Provider value={dispatch}>
            <div id={"bottom-notification-wrapper"} className={"notification-wrapper position-fixed"}>
                {state.map((note) => {
                    return <Notification dispatch={dispatch} key={note.id} {...note} />
                })}
            </div>
            {props.children}
        </NotificationContext.Provider>
    )
};

export const useNotificationCustom = () => {
    const dispatch = useContext(NotificationContext);

    return ({type, duration, message, title, ...props}) => {
        dispatch({
            type: "ADD_NOTIFICATION",
            payload: {
                id: v4(),
                dialogType: type,
                dialogDuration: duration,
                message: message,
                title: title,
                ...props
            }
        })
    }
};

export const useNotificationSuccessAndShort = () => {
    const dispatch = useContext(NotificationContext);

    return ({message, ...props}) => {
        dispatch({
            type: "ADD_NOTIFICATION",
            payload: {
                id: v4(),
                dialogType: dialogType.SUCCESS,
                dialogDuration: dialogDuration.SHORT,
                title: i18n.t("notification.title.success"),
                message: message,
                ...props
            }
        })
    }
};

export const useNotificationDangerAndLong = () => {
    const dispatch = useContext(NotificationContext);

    return ({message, ...props}) => {
        dispatch({
            type: "ADD_NOTIFICATION",
            payload: {
                id: v4(),
                dialogType: dialogType.DANGER,
                dialogDuration: dialogDuration.LONG,
                title: i18n.t("notification.title.danger"),
                message: message,
                ...props
            }
        })
    }
};

export const useNotificationWarningAndLong = () => {
    const dispatch = useContext(NotificationContext);

    return ({message, ...props}) => {
        dispatch({
            type: "ADD_NOTIFICATION",
            payload: {
                id: v4(),
                dialogType: dialogType.WARNING,
                dialogDuration: dialogDuration.LONG,
                title: i18n.t("notification.title.warning"),
                message: message,
                ...props
            }
        })
    }
};

export default NotificationProvider;