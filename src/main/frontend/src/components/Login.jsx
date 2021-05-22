import React, {useState} from "react";
import {useHistory} from "react-router";
import {useLocale} from "./LoginContext";
import styles from '../css/floatingbox.css';
import { withNamespaces } from 'react-i18next';
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import jwt_decode from "jwt-decode";

function Login(props) {
    const {t,i18n} = props
    const history = useHistory();
    const { token, setToken } = useLocale();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = e => {
        e.preventDefault()
        history.push("/userPage")

        const requestOptions = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ login: login, password: password }),
        };

        fetch('/resources/auth/auth', requestOptions)
            .then((res) => {
                if(res.status !== 202) {
                    throw Error('Invalid credentials')
                }
                return res.text()
            })
            .then((token) => {
                const tokenBearer = 'Bearer ' + token;
                setToken(tokenBearer);
                localStorage.setItem('token', tokenBearer)
                // czy to mądre ? todo: xd
                localStorage.setItem('username', jwt_decode(token)['sub'])
            })
            .catch(err => {
                console.log(err.message)
            })
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
