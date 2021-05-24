import React, {useEffect, useState} from "react";
import jwt_decode from "jwt-decode";
import {useNotificationCustom} from "./Notification/NotificationProvider";
import {dialogDuration, dialogType} from "./Notification/Notification";
import i18n from "../i18n";
import axios from "axios";

const REFRESH_TIME = 60 * 1000;
const LoginContext = React.createContext('');

export const LoginProvider = ({children}) => {
    const [token, setToken] = useState('');
    const [currentRole, setCurrentRole] = useState('');
    const [username, setUsername] = useState('');

    const dispatch = useNotificationCustom();

    const handleRefreshBox = () => {
        dispatch({
            "type": dialogType.WARNING,
            "duration": dialogDuration.MINUTE,
            "title": `${i18n.t('dialog.expiring_token.title')}`,
            "message":
                (<> {i18n.t('dialog.expiring_token.message')} <span className={"text-primary"} style={{cursor: "pointer"}}
                                                                    onClick={refreshToken}>{i18n.t('dialog.expiring_token.refresh')}</span></>)
        })
    }

    const refreshToken = (event) => {
        event.target.closest(".alert").querySelector(".close").click()

        axios.post('https://localhost:8181/resources/auth/refresh-token', localStorage.getItem("token"), {
            headers:{
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            }
        }).then(res => res.data)
            .then(token => saveToken(token));
        setTimeout(() => {
            schedule();
        }, 1000)
    }

    const schedule = () => {
        return setTimeout(() => {
            handleRefreshBox();
        }, new Date(jwt_decode(localStorage.getItem("token")).exp * 1000) - new Date() - REFRESH_TIME);
    }

    useEffect(() => {
        const storedToken = localStorage.getItem("token");
        if (storedToken === undefined || storedToken == null) return
        const decoded = jwt_decode(storedToken);
        const expirationDate = new Date(decoded.exp * 1000)
        console.log(`token expires at ${expirationDate}`)
        if (expirationDate < new Date()) {
            console.log("token expired")
            localStorage.removeItem("token")
            localStorage.removeItem("username")
            localStorage.removeItem("currentRole")
            setToken(null)
            setUsername('')
            setCurrentRole('')
        } else {
            setToken(storedToken)
        }
        setCurrentRole(localStorage.getItem('currentRole'));
        setUsername(localStorage.getItem('username'));
    }, [])

    useEffect(() =>{
        const storedToken = localStorage.getItem("token");
        if(storedToken === undefined || storedToken == null) return
        schedule()
    }, [])

    const saveToken = (value) => {
        setToken(value)
        localStorage.setItem("token", value)
    }

    const values = {
        token,
        setToken,
        saveToken,
        currentRole,
        setCurrentRole,
        username,
        setUsername
    }
    return <LoginContext.Provider value={values}>{children}</LoginContext.Provider>;
};

export const useLocale = () => React.useContext(LoginContext);