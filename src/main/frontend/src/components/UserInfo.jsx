import React, { Component } from "react";
import dog from "../images/dog_gradient.jpg";
import logo from "../images/logo.png";
import hand from "../images/hand.jpg";
import cat from "../images/cat.png";
import { withNamespaces } from "react-i18next";
import { Container, Button } from "react-bootstrap";
import "../css/UserInfo.css";

function UserInfo(props) {
  const { t, i18n } = props;
  const { isAuthenticated } = props;

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
      <h1>Moje konto</h1>
      <table>
        <tr>
          <td>Imie</td>
          <td>{userInfo.firstname}</td>
        </tr>
        <tr>
          <td>Nazwisko</td>
          <td>{userInfo.lastname}</td>
        </tr>
        <tr>
          <td>Adres email</td>
          <td>{userInfo.email}</td>
        </tr>
        <tr>
          <td>Login</td>
          <td>{userInfo.login}</td>
        </tr>
        <tr>
          <td>Numer telefonu</td>
          <td>{userInfo.contactNumber}</td>
        </tr>
        <tr>
          <td>Role</td>
          <td>{userRoles.join(',')}</td>
        </tr>
      </table>
      <div class="main-wrapper-actions-container">
        <Button className="btn-primary">Edytuj dane</Button>
        <Button className="btn-primary">Zmień hasło</Button>
      </div>
    </Container>
  );
}

export default withNamespaces()(UserInfo);
