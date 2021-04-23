import React, { Component } from "react";
import { Navbar, Nav } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

class NavigationBar extends Component {
    state = {};
    render() {
        return (
            <Navbar bg="light" expand="lg">
                <LinkContainer to="/">
                    <Navbar.Brand>Animal Hotel</Navbar.Brand>
                </LinkContainer>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
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
                        <LinkContainer to="/about">
                            <Nav.Link>
                                <FontAwesomeIcon className="mr-1" icon="sign-in-alt" />
                                About
                            </Nav.Link>
                        </LinkContainer>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        );
    }
}

export default NavigationBar;