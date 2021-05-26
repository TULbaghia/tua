import {withNamespaces} from "react-i18next";
import React, {useEffect} from "react";
import BreadCrumb from "./Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {useLocale} from "./LoginContext";
import {rolesConstant} from "../Constants";
import {getUserLanguage} from "./Partial/Navbar";
import {useDispatchThemeColorAfterLogin} from "./Utils/ThemeColor/ThemeColorProvider";

function AppUsersPage(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const {currentRole, username} = useLocale();
    const dispatchThemeChangeAfterLogin = useDispatchThemeColorAfterLogin()
    
    useEffect(() => {
       if (token) {
           getUserLanguage(token, i18n);
           dispatchThemeChangeAfterLogin(token);
       }
    }, []);

    return (
        <div className="Home">
            <BreadCrumb>
                <li className="breadcrumb-item">
                    <Link to="/">
                        <div className="back"> {t('mainPage')} </div>
                    </Link>
                </li>
                {currentRole === rolesConstant.admin && (
                    <li className="breadcrumb-item active" aria-current="page">{t('adminDashboard')}</li>)}
                {currentRole === rolesConstant.manager && (
                    <li className="breadcrumb-item active" aria-current="page">{t('managerDashboard')}</li>)}
                {currentRole === rolesConstant.client && (
                    <li className="breadcrumb-item active" aria-current="page">{t('userDashboard')}</li>)}
            </BreadCrumb>
            <div>
                <div className="greeting">{t('welcome')}, {username}</div>
            </div>
        </div>
    );
}

export default withNamespaces()(AppUsersPage);