import React, {useState} from "react";
import {useHistory} from "react-router";
import {useLocale} from "./LoginContext";
import { withNamespaces } from 'react-i18next';
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import { api } from "../Api";
import {handleRecaptcha} from "./Recaptcha/RecaptchaCallback";
import ReCAPTCHA from "react-google-recaptcha";
import {useNotificationWarningAndLong,} from "./Notification/NotificationProvider";

function SignUp(props) {
    const {t,i18n} = props
    const history = useHistory();
    const { token, setToken } = useLocale();
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [firstname, setFirstname] = useState('');
    const [lastname, setLastname] = useState('');
    const [constactNumber, setConstactNumber] = useState('');
    const recaptchaRef = React.createRef();
    const dispatchNotificationWarning = useNotificationWarningAndLong();

    const handleClick = (e) => {
        e.preventDefault()
        api.registerAccount({
            login: login,
            email: email,
            password: password,
            firstname: firstname,
            lastname: lastname,
            constactNumber: constactNumber
        })
        .then(res => {
            console.log("registered")
        })
        .catch(err => console.log(err))
    }


    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('signUp')}</li>
            </BreadCrumb>
            <div className="floating-box pt-2 pb-2">
                <form className="form-signup">
                    <h1 className="h3">{t('registering')}</h1>
                    <input
                        id="inputLogin"
                        className="form-control mt-3"
                        placeholder={t('login')}
                        required
                        autoFocus={true}
                        onChange={event => setLogin(event.target.value)}
                        style={{marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        id="inputEmail"
                        className="form-control"
                        placeholder={t('emailAddress')}
                        required
                        autoFocus={true}
                        onChange={event => setEmail(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        type="password"
                        id="inputPassword"
                        className="form-control"
                        placeholder={t('password')}
                        required
                        onChange={event => setPassword(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        type="password"
                        id="inputRepeatPassword"
                        className="form-control"
                        placeholder={t('repeatPassword')}
                        required
                        onChange={event => setPassword(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        id="inputName"
                        className="form-control"
                        placeholder={t('name')}
                        required
                        autoFocus={true}
                        onChange={event => setFirstname(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        id="inputSurname"
                        className="form-control"
                        placeholder={t('surname')}
                        required
                        autoFocus={true}
                        onChange={event => setLastname(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "1rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <input
                        id="inputPhoneNumber"
                        className="form-control"
                        placeholder={t('phoneNumber')}
                        required
                        autoFocus={true}
                        onChange={event => setConstactNumber(event.target.value)}
                        style={{marginTop: "1rem", marginBottom: "0rem", width: "90%", display: "inline-block"}}
                    />
                    <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
                    <div style={{color: "#7749F8", fontSize: 14, marginBottom: "3rem"}}>
                        {t('obligatoryFields')}
                    </div>
                    <button className="btn btn-lg btn-primary btn-block mb-3"
                            style={{backgroundColor: "#7749F8"}}
                            onClick={() => handleRecaptcha(handleClick, recaptchaRef, dispatchNotificationWarning)}>
                        {t('signUp')}
                    </button>
                </form>
                <ReCAPTCHA ref={recaptchaRef} sitekey={process.env.REACT_APP_RECAPTCHA_SITE_KEY}/>
            </div>
        </div>

    );

}

export default withNamespaces()(SignUp);