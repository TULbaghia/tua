import React, { Component } from "react";
import dog from "../images/dog_gradient.jpg";
import logo from "../images/logo.png";
import hand from "../images/hand.jpg";
import cat from "../images/cat.png";
import { withNamespaces } from "react-i18next";
import { Container, Button } from "react-bootstrap";
import "../css/UserInfo.css";
import { getToken } from "../store/authentication";

console.log(getToken())
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import { api } from "../Api";

function UserInfo(props) {
  const { t, i18n } = props;
  const { isAuthenticated } = props;

  let userInfo = {
    login: "",
    email: "",
    firstname: "",
    lastname: "",
    contactNumber: "",
  };

  const user = api.showAccountInformation()

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
            <tr>
              <td>{t("userDetailsFirstname")}</td>
              <td>{userInfo.firstname}</td>
            </tr>
            <tr>
              <td>{t("userDetailsLastname")}</td>
              <td>{userInfo.lastname}</td>
            </tr>
            <tr>
              <td>{t("userDetailsEmail")}</td>
              <td>{userInfo.email}</td>
            </tr>
            <tr>
              <td>{t("userDetailsLogin")}</td>
              <td>{userInfo.login}</td>
            </tr>
            <tr>
              <td>{t("userDetailsContactNumber")}</td>
              <td>{userInfo.contactNumber}</td>
            </tr>
            <tr>
              <td>{t("userDetailsRoles")}</td>
              <td>{userRoles.join(',')}</td>
            </tr>
          </table>
          <div className="main-wrapper-actions-container">
            <Button className="btn-primary">{t("userDetailsEditBtn")}</Button>
            <Button className="btn-primary">{t("userDetailsPasswordChangeBtn")}</Button>
          </div>
        </Container>
      </div>
  );
}

export default withNamespaces()(UserInfo);
