import React, {createContext, useContext, useReducer} from 'react';
import Notification from "./Notification";
import {v4} from "uuid";
import './NotificationWrapper.scss'

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
            <div className={"notification-wrapper position-fixed"}>
                {state.map((note) => {
                    return <Notification dispatch={dispatch} key={note.id} {...note} />
                })}
            </div>
            {props.children}
        </NotificationContext.Provider>
    )
};

export const useNotification = () => {
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

export default NotificationProvider;