import {withNamespaces} from "react-i18next";
import React, {useEffect} from "react";
import BreadCrumb from "./Partial/BreadCrumb";
import {Link} from "react-router-dom";
import {useLocale} from "./LoginContext";
import {rolesConstant} from "../Constants";
import {getUserLanguage} from "./Partial/Navbar";
import {useDispatchThemeColorAfterLogin} from "./Utils/ThemeColor/ThemeColorProvider";
import {useNotificationDangerAndInfinity} from "./Utils/Notification/NotificationProvider";
import {Container} from "@material-ui/core";

function AppUsersPage(props) {
    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const {currentRole, username} = useLocale();
    const dispatchThemeChangeAfterLogin = useDispatchThemeColorAfterLogin()
    const dispatchDangerError = useNotificationDangerAndInfinity();

    useEffect(() => {
        if (token) {
            getUserLanguage(token, i18n, () => dispatchThemeChangeAfterLogin(token), dispatchDangerError);
        }
    }, []);

    return (
        <Container fluid className="mb-2 container-fluid">
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
            <div className={"pt-0 pt-sm-4 pl-0 pl-sm-3"}>
                <div className="greeting mt-0 mt-sm-5 ml-0 ml-sm-5" style={{fontSize: "4rem", textShadow: "3px 3px #333"}}>{t('welcome')}, {username}</div>
            </div>
        </Container>
    );
}

export default withNamespaces()(AppUsersPage);