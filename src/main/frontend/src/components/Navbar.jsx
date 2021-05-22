import React, {Component} from "react";
import {Navbar, Nav, DropdownButton, Dropdown} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useHistory} from "react-router-dom";
import {useLocale} from "./LoginContext";
import {withNamespaces} from 'react-i18next';
import {library} from "@fortawesome/fontawesome-svg-core";
import {faUser} from "@fortawesome/free-solid-svg-icons";
import "../css/Navbar.css";
import DropdownToggle from "react-bootstrap/DropdownToggle";

library.add(faUser);

class LanguageSwitcher extends React.Component {

    constructor(props) {
        super(props);
        this.t = props.t
    }

    state = {
        langs: ['pl', 'en'],
        chosen: 0
    }

    handleClickPl = () => {
        this.setState({
            chosen: 0
        });
    }

    handleClickEn = () => {
        this.setState({
            chosen: 1
        })
    }

    render() {
        return (
            <>
                <p style={{fontSize: 20, color: "black", marginRight: "10px"}}>{this.state.langs[this.state.chosen]}</p>
                <Dropdown>
                    <DropdownToggle id="dropdown-basic" className="dropButton" variant="Info">
                    </DropdownToggle>

                    <Dropdown.Menu>
                        <Dropdown.Item onClick={this.handleClickPl}>{this.t(this.state.langs[0])}</Dropdown.Item>
                        <Dropdown.Item onClick={this.handleClickEn}>{this.t(this.state.langs[1])}</Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>
            </>
        )
    }
}

function NavigationBar(props) {
    const {t, i18n} = props
    const history = useHistory();
    const {token, setToken, setCurrentRole, setUsername} = useLocale();

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
                setCurrentRole('');
                setUsername('');
                localStorage.removeItem('token')
                // czy to mądre ? todo: xd
                localStorage.removeItem('currentRole')
                localStorage.removeItem('username')
            })
            .catch(err => console.log(err))
    }

    const {currentRole} = useLocale();

    const divStyle = () => {
        switch (currentRole) {
            case 'ADMIN':
                return {backgroundColor: "#EF5DA8"};
            case 'MANAGER':
                return {backgroundColor: "#F178B6"};
            case 'CLIENT':
                return {backgroundColor: "#EFADCE"};
        }
    };

    return (
        <>
            {token !== null && token !== '' ? (
                //------------------------LOGGED USER VIEW----------------------------
                <Navbar expand="lg" className="main-navbar" style={divStyle()}>
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
                            {currentRole === 'ADMIN' && (
                                <>
                                    <LinkContainer to="/accounts">
                                        <Nav.Link>{t('accountList')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/cities">
                                        <Nav.Link>{t('cityList')}</Nav.Link>
                                    </LinkContainer>
                                </>
                            )}
                            {currentRole === 'MANAGER' && (
                                <>
                                    <LinkContainer to="/myHotel">
                                        <Nav.Link>{t('myHotel')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/activeReservations">
                                        <Nav.Link>{t('activeReservations')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/archiveReservations">
                                        <Nav.Link>{t('archiveReservations')}</Nav.Link>
                                    </LinkContainer>
                                </>
                            )}
                            {currentRole === 'CLIENT' && (
                                <>
                                    <LinkContainer to="/cities">
                                        <Nav.Link>{t('cities')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/reservation">
                                        <Nav.Link>{t('reservation')}</Nav.Link>
                                    </LinkContainer>
                                </>
                            )}
                        </Nav>
                        <Nav className="navbar-right">
                            <LanguageSwitcher t={t}/>
                            <Dropdown alignRight={true}>
                                <Dropdown.Toggle id="dropdown-basic" className="dropButton" variant="Info">
                                    <FontAwesomeIcon icon="user"/>
                                    {' '}username{' '}
                                </Dropdown.Toggle>

                                <Dropdown.Menu>
                                    <li>
                                        <a href="#/action-1" className="item">
                                            <LinkContainer to="/myAccount">
                                                <Nav.Link>{t('myAccount')}</Nav.Link>
                                            </LinkContainer>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#/action-2" className="item">
                                            <LinkContainer to="/changePassword">
                                                <Nav.Link>{t('changePassword')}</Nav.Link>
                                            </LinkContainer>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="#/action-3" className="item">
                                            <LinkContainer to="/">
                                                <Nav.Link onSelect={handleLogout}>{t('signOut')}</Nav.Link>
                                            </LinkContainer>
                                        </a>
                                    </li>
                                </Dropdown.Menu>
                            </Dropdown>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            ) : (
                //------------------------GUEST VIEW----------------------------
                <Navbar expand="lg" className="main-navbar" style={divStyle()}>
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
                            <LanguageSwitcher t={t}/>
                            {token !== null && token !== '' ? (
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
            )}
        </>
    )

    // *** LANDING PAGE ***
    // const {isAuthenticated} = props;
    // return (
    //     // <Navbar bg="light" expand="lg" className="main-navbar">
    //     <Navbar expand="lg" className="main-navbar" style={divStyle()}>
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
    //                 <LinkContainer to="/regulations">
    //                     <Nav.Link>{t('regulations')}</Nav.Link>
    //                 </LinkContainer>
    //             </Nav>
    //             <Nav className="navbar-right">
    //                 <LanguageSwitcher t={t}/>
    //                 {token !== null && token !== '' ? (
    //                     <Nav.Link onClick={handleLogout}>Logout</Nav.Link>
    //                 ) : (
    //                     <>
    //                         <LinkContainer to="/signUp">
    //                             <Nav.Link className="signUp">
    //                                 <FontAwesomeIcon className="signUpIcon" icon="user-plus"/>
    //                                 <div className="signUpText">{t('signUp')}</div>
    //                             </Nav.Link>
    //                         </LinkContainer>
    //                         <LinkContainer to="/login">
    //                             <Nav.Link className="login">
    //                                 <FontAwesomeIcon className="loginIcon" icon="sign-in-alt"/>
    //                                 <div className="loginText">{t('signIn')}</div>
    //                             </Nav.Link>
    //                         </LinkContainer>
    //                     </>
    //                 )}
    //
    //             </Nav>
    //         </Navbar.Collapse>
    //     </Navbar>
    // );


    // *** ADMIN ***
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
    //                 <LanguageSwitcher t={t}/>
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
    //                                     <Nav.Link onSelect={handleLogout}>{t('signOut')}</Nav.Link>
    //                                 </LinkContainer>
    //                             </a>
    //                         </li>
    //                     </Dropdown.Menu>
    //                 </Dropdown>
    //             </Nav>
    //         </Navbar.Collapse>
    //     </Navbar>
    // );
    //
    //
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
    //                 <LanguageSwitcher t={t}/>
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
    //
    //
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
    //                 <LanguageSwitcher t={t}/>
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