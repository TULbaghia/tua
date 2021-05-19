import React, {Component} from "react";
import {Navbar, Nav, Dropdown, DropdownButton} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useHistory} from "react-router-dom";
import {useLocale} from "./LoginContext";
import { withNamespaces } from 'react-i18next';
import { library } from "@fortawesome/fontawesome-svg-core";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import "../css/Navbar.css";

library.add(faUser);

function NavigationBar(props) {
    const {t,i18n} = props
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

    // *** LANDING PAGE ***
    const {isAuthenticated} = props;
    return (
        <Navbar bg="light" expand="lg">
            <Navbar.Brand>
                <div className="name">{t('animalHotel')}</div>
            </Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav"/>
            <Navbar.Collapse id="basic-navbar-nav">
                <Nav className="mr-auto">
                    <LinkContainer to="/">
                        <Nav.Link>{t('mainPage')}</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/hotels">
                        <Nav.Link>{t('hotels')}</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/regulations">
                        <Nav.Link>{t('regulations')}</Nav.Link>
                    </LinkContainer>
                </Nav>
                <Nav className="navbar-right">
                    {token!==null && token !== '' ? (
                        <Nav.Link onClick={handleLogout}>Logout</Nav.Link>
                    ) : (
                        <>
                            <LinkContainer to="/signUp">
                                <Nav.Link className="signUp">
                                    <FontAwesomeIcon className="signUpIcon" icon="user-plus"/>
                                    <div className="signUpText">{t('signUp')}</div>
                                </Nav.Link>
                            </LinkContainer>
                            <LinkContainer to="/login">
                                <Nav.Link className="login">
                                    <FontAwesomeIcon className="loginIcon" icon="sign-in-alt"/>
                                    <div className="loginText">{t('signIn')}</div>
                                </Nav.Link>
                            </LinkContainer>
                        </>
                    )}

                </Nav>
            </Navbar.Collapse>
        </Navbar>
    );


    // // *** ADMIN ***
    // const {isAuthenticated} = props;
    // return (
    //     <Navbar  expand="lg" className="color-nav">
    //         <Navbar.Brand>
    //             <div className="name">{t('animalHotel')}</div>
    //         </Navbar.Brand>
    //         <Navbar.Toggle aria-controls="basic-navbar-nav"/>
    //         <Navbar.Collapse id="basic-navbar-nav">
    //             <Nav className="mr-auto">
    //                 <LinkContainer to="/">
    //                     <Nav.Link>{t('mainPage')}</Nav.Link>
    //                 </LinkContainer>
    //                 <LinkContainer to="/hotels">
    //                     <Nav.Link>{t('hotels')}</Nav.Link>
    //                 </LinkContainer>
    //                 <LinkContainer to="/accounts">
    //                     <Nav.Link>{t('accountList')}</Nav.Link>
    //                 </LinkContainer>
    //                 <LinkContainer to="/cities">
    //                     <Nav.Link>{t('cityList')}</Nav.Link>
    //                 </LinkContainer>
    //             </Nav>
    //             <Nav className="navbar-right">
    //                 <Dropdown alignRight={true}>
    //                     <Dropdown.Toggle id="dropdown-basic" className="dropButton" variant="Info">
    //                         <FontAwesomeIcon icon="user"/>
    //                         {' '}username{' '}
    //                     </Dropdown.Toggle>
    //
    //                     <Dropdown.Menu>
    //                         <li>
    //                             <a href="#/action-1" className="item">
    //                                 <LinkContainer to="/myAccount">
    //                                     <Nav.Link>{t('myAccount')}</Nav.Link>
    //                                 </LinkContainer>
    //                             </a>
    //                         </li>
    //                         <li>
    //                             <a href="#/action-2" className="item">
    //                                 <LinkContainer to="/changePassword">
    //                                     <Nav.Link>{t('changePassword')}</Nav.Link>
    //                                 </LinkContainer>
    //                             </a>
    //                         </li>
    //                         <li>
    //                             <a href="#/action-3" className="item">
    //                                 <LinkContainer to="/">
    //                                     <Nav.Link>{t('signOut')}</Nav.Link>
    //                                 </LinkContainer>
    //                             </a>
    //                         </li>
    //                     </Dropdown.Menu>
    //                 </Dropdown>
    //             </Nav>
    //         </Navbar.Collapse>
    //     </Navbar>
    // );


    // // *** MANAGER ***
    // const {isAuthenticated} = props;
    // return (
    //     <Navbar  expand="lg" className="color-nav">
    //         <Navbar.Brand>
    //             <div className="name">{t('animalHotel')}</div>
    //         </Navbar.Brand>
    //         <Navbar.Toggle aria-controls="basic-navbar-nav"/>
    //         <Navbar.Collapse id="basic-navbar-nav">
    //             <Nav className="mr-auto">
    //                 <LinkContainer to="/">
    //                     <Nav.Link>{t('mainPage')}</Nav.Link>
    //                 </LinkContainer>
    //                 <LinkContainer to="/hotels">
    //                     <Nav.Link>{t('hotels')}</Nav.Link>
    //                 </LinkContainer>
    //                 <LinkContainer to="/myHotel">
    //                     <Nav.Link>{t('myHotel')}</Nav.Link>
    //                 </LinkContainer>
    //                 <LinkContainer to="/activeReservations">
    //                     <Nav.Link>{t('activeReservations')}</Nav.Link>
    //                 </LinkContainer>
    //                 <LinkContainer to="/archiveReservations">
    //                     <Nav.Link>{t('archiveReservations')}</Nav.Link>
    //                 </LinkContainer>
    //             </Nav>
    //             <Nav className="navbar-right">
    //                 <Dropdown alignRight={true}>
    //                     <Dropdown.Toggle id="dropdown-basic" className="dropButton" variant="Info">
    //                         <FontAwesomeIcon icon="user"/>
    //                         {' '}username{' '}
    //                     </Dropdown.Toggle>
    //
    //                     <Dropdown.Menu>
    //                         <li>
    //                             <a href="#/action-1" className="item">
    //                                 <LinkContainer to="/myAccount">
    //                                     <Nav.Link>{t('myAccount')}</Nav.Link>
    //                                 </LinkContainer>
    //                             </a>
    //                         </li>
    //                         <li>
    //                             <a href="#/action-2" className="item">
    //                                 <LinkContainer to="/changePassword">
    //                                     <Nav.Link>{t('changePassword')}</Nav.Link>
    //                                 </LinkContainer>
    //                             </a>
    //                         </li>
    //                         <li>
    //                             <a href="#/action-3" className="item">
    //                                 <LinkContainer to="/">
    //                                     <Nav.Link>{t('signOut')}</Nav.Link>
    //                                 </LinkContainer>
    //                             </a>
    //                         </li>
    //                     </Dropdown.Menu>
    //                 </Dropdown>
    //             </Nav>
    //         </Navbar.Collapse>
    //     </Navbar>
    // );


    // *** USER ***
    // const {isAuthenticated} = props;
    // return (
    //     <Navbar  expand="lg" className="color-nav">
    //         <Navbar.Brand>
    //             <div className="name">{t('animalHotel')}</div>
    //         </Navbar.Brand>
    //         <Navbar.Toggle aria-controls="basic-navbar-nav"/>
    //         <Navbar.Collapse id="basic-navbar-nav">
    //             <Nav className="mr-auto">
    //                 <LinkContainer to="/">
    //                     <Nav.Link>{t('mainPage')}</Nav.Link>
    //                 </LinkContainer>
    //                 <LinkContainer to="/hotels">
    //                     <Nav.Link>{t('hotels')}</Nav.Link>
    //                 </LinkContainer>
    //                 <LinkContainer to="/cities">
    //                     <Nav.Link>{t('cities')}</Nav.Link>
    //                 </LinkContainer>
    //                 <LinkContainer to="/reservation">
    //                     <Nav.Link>{t('reservation')}</Nav.Link>
    //                 </LinkContainer>
    //             </Nav>
    //             <Nav className="navbar-right">
    //                 <Dropdown alignRight={true}>
    //                     <Dropdown.Toggle id="dropdown-basic" className="dropButton" variant="Info">
    //                         <FontAwesomeIcon icon="user"/>
    //                         {' '}username{' '}
    //                     </Dropdown.Toggle>
    //
    //                     <Dropdown.Menu>
    //                         <li>
    //                             <a href="#/action-1" className="item">
    //                                 <LinkContainer to="/myAccount">
    //                                     <Nav.Link>{t('myAccount')}</Nav.Link>
    //                                 </LinkContainer>
    //                             </a>
    //                         </li>
    //                         <li>
    //                             <a href="#/action-2" className="item">
    //                                 <LinkContainer to="/changePassword">
    //                                     <Nav.Link>{t('changePassword')}</Nav.Link>
    //                                 </LinkContainer>
    //                             </a>
    //                         </li>
    //                         <li>
    //                             <a href="#/action-3" className="item">
    //                                 <LinkContainer to="/">
    //                                     <Nav.Link>{t('signOut')}</Nav.Link>
    //                                 </LinkContainer>
    //                             </a>
    //                         </li>
    //                     </Dropdown.Menu>
    //                 </Dropdown>
    //             </Nav>
    //         </Navbar.Collapse>
    //     </Navbar>
    // );
}

export default withNamespaces()(NavigationBar);