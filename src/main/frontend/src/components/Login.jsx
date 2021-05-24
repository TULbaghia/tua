import React, {useState} from "react";
import {useHistory} from "react-router";
import {useLocale} from "./LoginContext";
import { withNamespaces } from 'react-i18next';
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import { api } from "../Api";
import {dialogDuration, dialogType} from "./Notification/Notification";
import axios from "axios";
import jwt_decode from "jwt-decode";
import {useRefreshNotificationCustom} from "./Notification/RefreshNotificationProvider";

const REFRESH_TIME = 60 * 1000;

function Login(props) {
    const {t,i18n} = props
    const history = useHistory();
    const { token, setToken, saveToken } = useLocale();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(false);

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

    const handleLogin = async e => {
        e.preventDefault()
        try{
            setError(false)
            const res = await api.login({
                login: login,
                password: password
            })
            saveToken(res.data)
            history.push("/home")
        }catch(ex){
            console.log(ex);
            setError(true);
        }
        schedule();
    }

    const schedule = () => {
        return setTimeout(() => {
            handleRefreshBox();
        }, new Date(jwt_decode(localStorage.getItem("token")).exp * 1000) - new Date() - REFRESH_TIME);
    }

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('logging')}</li>
            </BreadCrumb>
            <div className="floating-box">
                <form className="form-signin" onSubmit={handleLogin}>
                    <h1 className="h3">{t('logging')}</h1>
                    {error && <span className="login-error-span">{t('signInError')}</span>}
                    <input
                        id="inputLogin"
                        className="form-control"
                        placeholder={t('login')}
                        required
                        autoFocus={true}
                        onChange={event => setLogin(event.target.value)}
                        style={{marginTop: "3rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        type="password"
                        id="inputPassword"
                        className="form-control"
                        placeholder={t('password')}
                        required
                        onChange={event => setPassword(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "3rem", width: "90%", display: "inline-block"}}
                    />
                    <span style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</span>
                    <button className="btn btn-lg btn-primary btn-block" type="submit" style={{backgroundColor: "#7749F8"}}>
                        {t('signIn')}
                    </button>
                    <button className="btn btn-lg btn-primary btn-block" type="button" onClick={() => history.push("/login/password-reset")} style={{backgroundColor: "#7749F8"}}>
                        {t('passwordReset')}
                    </button>
                </form>
            </div>
        </div>

    );

}

export default withNamespaces()(Login);