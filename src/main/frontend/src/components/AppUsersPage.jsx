import {withNamespaces} from "react-i18next";
import React, {useEffect, useLayoutEffect, useReducer, useState} from "react";
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";

function AppUsersPage(props) {
    const {t, i18n} = props
    const {isAuthenticated} = props;
    const [login, setLogin] = useState('')
    const role = "ADMIN"

    useEffect(() => {
        // if(login !== '') {
            setLogin(localStorage.getItem('username'))
        // }
    }, [login])

    if (role === "ADMIN") {
        // // *** ADMIN ***
        return (
            <div className="Home">
                <BreadCrumb>
                    <li className="breadcrumb-item">
                        <Link to="/">
                            <div className="back"> {t('mainPage')} </div>
                        </Link>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">{t('adminDashboard')}</li>
                </BreadCrumb>
                <div>
                    <div className="greeting">{t('welcome')}, {login}</div>
                </div>
            </div>
        );
    } else if (role === "MANAGER") {
        // *** MANAGER ***
        return (
            <div className="Home">
                <BreadCrumb>
                    <li className="breadcrumb-item">
                        <Link to="/">
                            <div className="back"> {t('mainPage')} </div>
                        </Link>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">{t('managerDashboard')}</li>
                </BreadCrumb>
                <div>
                    <div className="greeting">{t('welcome')}, username</div>
                </div>
            </div>
        );
    } else {
        // *** USER ***
        return (
            <div className="Home">
                <BreadCrumb>
                    <li className="breadcrumb-item">
                        <Link to="/">
                            <div className="back"> {t('mainPage')} </div>
                        </Link>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">{t('userDashboard')}</li>
                </BreadCrumb>
                <div>
                    <div className="greeting">{t('welcome')}, username</div>
                </div>
            </div>
        );
    }
}

export default withNamespaces()(AppUsersPage);