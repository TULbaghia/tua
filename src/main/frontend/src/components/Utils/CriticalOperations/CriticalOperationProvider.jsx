import React, {createContext, useContext, useReducer} from "react";
import Dialog from "./Dialog";
import {v4} from "uuid";
import i18n from '../../../i18n';

const CriticalOperation = createContext();

const CriticalOperationProvider = (props) => {
    const [state, dispatch] = useReducer((state, action) => {
        switch (action.type) {
            case "ADD_DIALOG":
                return [...state, {...action.payload}];
            case "REMOVE_DIALOG":
                return state.filter(e => e.id !== action.id);
            default:
                return state;
        }
    }, []);

    return (
        <CriticalOperation.Provider value={dispatch}>
            {state.map((note) => {
                return <Dialog dispatch={dispatch} key={note.id} {...note} />
            })}
            {props.children}
        </CriticalOperation.Provider>
    )
};

export const useDialog = () => {
    const dispatch = useContext(CriticalOperation);

    return ({title, message, callbackOnSave = (() => {}), callbackOnCancel = (() => {}), ...props}) => {
        dispatch({
            type: "ADD_DIALOG",
            payload: {
                id: v4(),
                title: title,
                message: message,
                callbackOnSave: callbackOnSave,
                callbackOnCancel: callbackOnCancel,
                ...props
            }
        });
    }
};

export const useDialogPermanentChange = ({title = "dialog.permanent_change.title", message = "dialog.permanent_change.message"}) => {
    const dispatch = useContext(CriticalOperation);

    return ({callbackOnSave = (() => {}), callbackOnCancel = (() => {}), ...props}) => {
        dispatch({
            type: "ADD_DIALOG",
            payload: {
                id: v4(),
                title: i18n.t(title),
                message: i18n.t(message),
                callbackOnSave: callbackOnSave,
                callbackOnCancel: callbackOnCancel,
                ...props
            }
        });
    }
};

export default CriticalOperationProvider;