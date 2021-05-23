import React, {useEffect, useState} from "react";
import jwt_decode from "jwt-decode";

const LoginContext = React.createContext('');

export const LoginProvider = ({children}) => {
    const [token, setToken] = useState('');
    const [currentRole, setCurrentRole] = useState('');
    const [username, setUsername] = useState('');

    useEffect(() => {
        const storedToken = localStorage.getItem("token");
        if (storedToken === undefined || storedToken == null) return
        const decoded = jwt_decode(storedToken);
        const expirationDate = new Date(decoded.exp * 1000)
        console.log(`token expires at ${expirationDate}`)
        if (expirationDate < new Date()) {
            console.log("token expired")
            localStorage.removeItem("token")
            setToken(null)
        } else {
            setToken(storedToken)
        }
        setCurrentRole(localStorage.getItem('currentRole'));
        setUsername(localStorage.getItem('username'));
    })

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