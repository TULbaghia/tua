import React, {Component} from "react";
import {Navbar, Nav} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import AuthService from "../AuthService";
import {useHistory} from "react-router-dom";

function NavigationBar(props) {
    const history = useHistory();

    const handleLogout = () => {
        localStorage.removeItem("isLogged");
        AuthService.setToken('')
        history.push("/login")
        console.log("logout")
    }

        const {isAuthenticated} = props;
        return (
            <Navbar bg="light" expand="lg">
                <LinkContainer to="/">
                    <Navbar.Brand>Animal Hotel</Navbar.Brand>
                </LinkContainer>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">
                        <LinkContainer to="/blog">
                            <Nav.Link>Blog</Nav.Link>
                        </LinkContainer>
                        <LinkContainer to="/pong">
                            <Nav.Link>Pong</Nav.Link>
                        </LinkContainer>
                    </Nav>
                    <Nav className="navbar-right">
                        {isAuthenticated ? (
                            <Nav.Link onClick={handleLogout}>Logout</Nav.Link>
                        ) : (
                            <>
                                <LinkContainer to="/login">
                                    <Nav.Link>
                                        <FontAwesomeIcon className="mr-1" icon="user-plus"/>
                                        Signup
                                    </Nav.Link>
                                </LinkContainer>
                                <LinkContainer to="/login">
                                    <Nav.Link>
                                        <FontAwesomeIcon className="mr-1" icon="sign-in-alt"/>
                                        Login
                                    </Nav.Link>
                                </LinkContainer>
                            </>
                        )}

                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        );
}

export default NavigationBar;