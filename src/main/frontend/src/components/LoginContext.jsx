import React, {useEffect, useState} from "react";
import jwt_decode from "jwt-decode";
import {useNotificationCustom, useNotificationSuccessAndShort} from "./Utils/Notification/NotificationProvider";
import {dialogDuration, dialogType} from "./Utils/Notification/Notification";
import i18n from "../i18n";
import axios from "axios";
import {useDispatchThemeColorAfterLogin} from "./Utils/ThemeColor/ThemeColorProvider";

const REFRESH_TIME = 60 * 1000;
const LoginContext = React.createContext('');

export const LoginProvider = ({children}) => {
    const [token, setToken] = useState('');
    const [currentRole, setCurrentRole] = useState('');
    const [username, setUsername] = useState('');

    const dispatch = useNotificationCustom();
    const dispatchThemeChangeAfterLogin = useDispatchThemeColorAfterLogin()
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();

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

        axios.post(`${process.env.REACT_APP_API_BASE_URL}/resources/auth/refresh-token`, localStorage.getItem("token"), {
            headers:{
                "Authorization": `${localStorage.getItem("token")}`
            }
        }).then(res => res.data)
            .then(token =>{saveToken("Bearer " + token);
                schedule();
                dispatchNotificationSuccess({message: i18n.t('tokenRefreshSuccess')})});
    }

    const schedule = () => {
        const id = setTimeout(() => {
            handleRefreshBox();
        }, new Date(jwt_decode(localStorage.getItem("token")).exp * 1000) - new Date() - REFRESH_TIME);
        localStorage.setItem("timeoutId", id.toString())
    }

    useEffect(() => {
        const storedToken = localStorage.getItem("token");
        if (storedToken === undefined || storedToken == null) return
        const decoded = jwt_decode(storedToken);
        const expirationDate = new Date(decoded.exp * 1000)
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
        dispatchThemeChangeAfterLogin(value);
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
    return (<LoginContext.Provider value={values}>{children}</LoginContext.Provider>);
};

export const useLocale = () => React.useContext(LoginContext);