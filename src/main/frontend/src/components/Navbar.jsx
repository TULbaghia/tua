import React, {Component} from "react";
import {Navbar, Nav} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useHistory} from "react-router-dom";
import {useLocale} from "./LoginContext";
import { withNamespaces } from 'react-i18next';

function NavigationBar(props) {
    const  {t,i18n} = props
    const history = useHistory();
    const {token, setToken} = useLocale();

    const handleLogout = () => {
        history.push("/login")
        const requestOptions = {
            method: "GET",
            headers: {
                Authorization: token,
            }
        };
        fetch('/resources/auth/logout', requestOptions)
            .then((res) => {
                setToken('');
                localStorage.removeItem('token')
            })
            .catch(err => console.log(err))
    }

    const {isAuthenticated} = props;
    return (
        <Navbar bg="light" expand="lg">
            <Navbar.Brand>{t('animalHotel')}</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav"/>
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="mr-auto">
                    <LinkContainer to="/">
                        <Nav.Link>{t('mainPage')}</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/hotele">
                        <Nav.Link>{t('hotels')}</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/regulamin">
                        <Nav.Link>{t('regulations')}</Nav.Link>
                    </LinkContainer>
                </Nav>
                <Nav className="navbar-right">
                    {token!==null && token !== '' ? (
                        <Nav.Link onClick={handleLogout}>Logout</Nav.Link>
                    ) : (
                        <>
                            <LinkContainer to="/signUp">
                                <Nav.Link>
                                    <FontAwesomeIcon className="mr-1" icon="user-plus"/>
                                    {t('signUp')}
                                </Nav.Link>
                            </LinkContainer>
                            <LinkContainer to="/login">
                                <Nav.Link>
                                    <FontAwesomeIcon className="mr-1" icon="sign-in-alt"/>
                                    {t('signIn')}
                                </Nav.Link>
                            </LinkContainer>
                        </>
                    )}

                </Nav>
            </Navbar.Collapse>
        </Navbar>
    );
}

export default withNamespaces()(NavigationBar);