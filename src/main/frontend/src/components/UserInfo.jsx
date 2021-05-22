import React, {useEffect, useState} from "react";
import {withNamespaces} from "react-i18next";
import {Container, Button, Table} from "react-bootstrap";
import "../css/UserInfo.css";
import { getToken } from "../store/authentication";

import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import {api} from "../Api";
import {useHistory} from "react-router";

function UserInfo(props) {
    const {t, i18n} = props;
    const {isAuthenticated} = props;
    const history = useHistory();
    const [data, setData] = useState({
        login: "",
        email: "",
        firstname: "",
        lastname: "",
        contactNumber: "",
    });

    // api.showAccountInformation({headers: {Authorization: "Bearer " + getToken().data}}).then((res) => {
    //     // userInfo.login = res.login;
    //     userInfo.login = "asd";
    // }).catch((err) => {
    //     console.log(err.toString())
    //     if(err.response.status === 403) {
    //         history.push("/errors/forbidden")
    //     }
    // });

    React.useEffect(() => {
        if(getToken()) {
            getUser().then(res => {
                let userInfo = data;
                userInfo.login = "res.login";
                setData(userInfo);
            });
        }
    }, []);

    const getUser = async () => {
        return await api.showAccountInformation({headers: {Authorization: "Bearer " + getToken().data}})
    }

    const userRoles = [
        "MANAGER",
        "CLIENT"
    ]

    return (
        <div className="container">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/">{t('mainPage')}</Link></li>
                <li className="breadcrumb-item active" aria-current="page">{t('accountInfo')}</li>
            </BreadCrumb>
            <Container className="main-wrapper floating-box">
                <h1>{t("userDetailsTitle")}</h1>
                <table>
                    <thead/>
                    <tbody>
                        <tr>
                            <td>{t("userDetailsFirstname")}</td>
                            <td>{data.firstname}</td>
                        </tr>
                        <tr>
                            <td>{t("userDetailsLastname")}</td>
                            <td>{data.lastname}</td>
                        </tr>
                        <tr>
                            <td>{t("userDetailsEmail")}</td>
                            <td>{data.email}</td>
                        </tr>
                        <tr>
                            <td>{t("userDetailsLogin")}</td>
                            <td>{data.login}</td>
                        </tr>
                        <tr>
                            <td>{t("userDetailsContactNumber")}</td>
                            <td>{data.contactNumber}</td>
                        </tr>
                        <tr>
                            <td>{t("userDetailsRoles")}</td>
                            <td>{userRoles.join(',')}</td>
                        </tr>
                    </tbody>
                </table>
                <div className="main-wrapper-actions-container">
                    <Button className="btn-primary">{t("userDetailsEditBtn")}</Button>
                </div>
            </Container>
        </div>
    );
}

export default withNamespaces()(UserInfo);
