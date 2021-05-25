import {withNamespaces} from "react-i18next";
import React, {useEffect} from "react";
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import {useLocale} from "./LoginContext";
import {getUserLanguage} from "./Navbar";

function AppUsersPage(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const {currentRole, username} = useLocale();

   useEffect(() => {
       if (token) {
           getUserLanguage(token, i18n);
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
                {currentRole === "ADMIN" && (
                    <li className="breadcrumb-item active" aria-current="page">{t('adminDashboard')}</li>)}
                {currentRole === "MANAGER" && (
                    <li className="breadcrumb-item active" aria-current="page">{t('managerDashboard')}</li>)}
                {currentRole === "CLIENT" && (
                    <li className="breadcrumb-item active" aria-current="page">{t('userDashboard')}</li>)}
            </BreadCrumb>
            <div>
                <div className="greeting">{t('welcome')}, {username}</div>
            </div>
        </div>
    );
}

export default withNamespaces()(AppUsersPage);