import React, { Component } from "react";
import dog from "../images/dog_gradient.jpg";
import logo from "../images/logo.png";
import hand from "../images/hand.jpg";
import cat from "../images/cat.png";
import { withNamespaces } from "react-i18next";
import { Container, Button } from "react-bootstrap";
import "../css/UserInfo.css";
import { useLocale } from "./LoginContext";
import {getUserLanguage} from "./Navbar";

function UserInfo(props) {
  const { t, i18n } = props;
  const { isAuthenticated } = props;
  const {token, setToken} = useLocale();

  console.log(token)
  getUserLanguage(token, i18n)

  const userInfo = {
    login: "mszewc",
    email: "mszewc@edu.pl",
    firstname: "Mateusz",
    lastname: "Szewc",
    contactNumber: "909 909 909",
  };

  const userRoles = [
      "MANAGER",
      "CLIENT"
  ]

  return (
    <Container className="main-wrapper">
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
      <div class="main-wrapper-actions-container">
        <Button className="btn-primary">{t("userDetailsEditBtn")}</Button>
        <Button className="btn-primary">{t("userDetailsPasswordChangeBtn")}</Button>
      </div>
    </Container>
  );
}

export default withNamespaces()(UserInfo);
