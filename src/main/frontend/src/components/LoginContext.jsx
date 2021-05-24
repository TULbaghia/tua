import React, {createContext, useEffect, useState} from "react";
import jwt_decode from "jwt-decode";
import {useNotificationCustom} from "./Notification/NotificationProvider";
import {dialogDuration, dialogType} from "./Notification/Notification";
import i18n from "../i18n";
import axios from "axios";
import {useRefreshNotificationCustom} from "./Notification/RefreshNotificationProvider";

const LoginContext = React.createContext('');

export const LoginProvider = ({ children }) => {
    const [token, setToken] = useState('');

    const dispatch = useRefreshNotificationCustom();

    const handleRefreshBox = () => {
        dispatch({
            "type": dialogType.WARNING,
            "duration": dialogDuration.MINUTE,
            "title": `${i18n.t('dialog.expiring_token.title')}`,
            "message":
                (<> {i18n.t('dialog.expiring_token.message')} <span className={"text-primary"}
                                                                    onClick={refreshToken}>{i18n.t('dialog.expiring_token.refresh')}</span></>)
        })
    }

    const refreshToken = () => {
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
    if(storedToken === undefined || storedToken == null) return
    const decoded = jwt_decode(storedToken);
    const expirationDate = new Date(decoded.exp * 1000)
    console.log(`token expires at ${expirationDate}`)
    if(expirationDate - new Date() < 60000 && expirationDate > new Date()){
        handleRefreshBox()
    }
    if(expirationDate < new Date()){
        console.log("token expired")
        localStorage.removeItem("token")
        setToken(null)

    }else{
        setToken(storedToken)
    }
})

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
    saveToken
}

return <LoginContext.Provider value={values}>{children}</LoginContext.Provider>;
};

export const useLocale = () => React.useContext(LoginContext);

export function Refresh(){
    console.log("refresh")

}