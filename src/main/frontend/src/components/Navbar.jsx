import React, {Component} from "react";
import {Navbar, Nav, DropdownButton, Dropdown} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useHistory} from "react-router-dom";
import {useLocale} from "./LoginContext";
import { withNamespaces } from 'react-i18next';
import "../css/Navbar.css"
import DropdownToggle from "react-bootstrap/DropdownToggle";

class LanguageSwitcher extends React.Component{

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
        return(
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
                    <LanguageSwitcher t={t}/>
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
}

export default withNamespaces()(NavigationBar);