import React, {useEffect, useState} from "react";

const LoginContext = React.createContext('');

export const LoginProvider = ({ children }) => {
    const [token, setToken] = useState('');

    useEffect(() => {
        setToken(localStorage.getItem('token'))
    }, [])

    const values = {
        token,
        setToken
    }
    return <LoginContext.Provider value={values}>{children}</LoginContext.Provider>;
};

export const useLocale = () => React.useContext(LoginContext);