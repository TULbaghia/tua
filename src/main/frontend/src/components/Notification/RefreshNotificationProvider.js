import React, {createContext, useContext, useReducer} from 'react';
import {v4} from "uuid";
import './NotificationWrapper.scss'
import RefreshNotification, {dialogDuration, dialogType} from "./RefreshNotification";

const RefreshNotificationContext = createContext();

const RefreshNotificationProvider = (props) => {
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
        <RefreshNotificationContext.Provider value={dispatch}>
            <div id={"bottom-notification-wrapper"} className={"notification-wrapper position-fixed"}>
                {state.map((note) => {
                    return <RefreshNotification dispatch={dispatch} key={note.id} {...note} />
                })}
            </div>
            {props.children}
        </RefreshNotificationContext.Provider>
    )
};

export const useRefreshNotificationCustom = () => {
    const dispatch = useContext(RefreshNotificationContext);

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


export default RefreshNotificationProvider;