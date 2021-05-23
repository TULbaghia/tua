import React, {useEffect, useState} from "react";

const LoginContext = React.createContext('');

export const LoginProvider = ({ children }) => {
    const [token, setToken] = useState('');
    const [currentRole, setCurrentRole] = useState('');
    const [userName, setUserName] = useState('');

    useEffect(() => {
        // getToken().then(res => setToken(res)).then(()=>{
        //     setToken(localStorage.getItem('currentRole'));
        //     setToken(localStorage.getItem('username'));
        // })
        setToken(localStorage.getItem('token'));
            setCurrentRole(localStorage.getItem('currentRole'));
            setUserName(localStorage.getItem('username'));
    }, [])

    const getToken = async () => {
        const res = await localStorage.getItem('token');
        return res;
    }

    const values = {
        token,
        setToken,
        currentRole,
        setCurrentRole,
        userName,
        setUserName
    }
    return <LoginContext.Provider value={values}>{children}</LoginContext.Provider>;
};

export const useLocale = () => React.useContext(LoginContext);