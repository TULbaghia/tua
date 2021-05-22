import React, {useState} from "react";
import {useHistory} from "react-router";
import {useLocale} from "./LoginContext";
import styles from '../css/floatingbox.css';
import { withNamespaces } from 'react-i18next';
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import { api } from "../Api";
import { getToken, signIn } from "../store/authentication";
import store from "../store";

function Login(props) {
    const {t,i18n} = props
    const history = useHistory();
    const { token, setToken } = useLocale();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async e => {
        e.preventDefault()
        await signIn(login, password)

        console.log(getToken())

        history.push("/home")

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
