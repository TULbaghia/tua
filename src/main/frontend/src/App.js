import React, {useEffect, useState} from 'react';
import {BrowserRouter as Router, HashRouter, Route, Switch} from 'react-router-dom';
import './App.css';
import NavigationBar from "./components/Navbar";
import {library} from "@fortawesome/fontawesome-svg-core";
import {fab} from "@fortawesome/free-brands-svg-icons";
import {faSignInAlt, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import Home from "./components/Home";
import Login from "./components/Login";
import SignUp from "./components/SignUp";
import Footer from "./components/Footer";
import ConfirmedAccount from "./components/ConfirmedAccount";
import PasswordReset from "./components/PasswordReset";
import NotFound from "./components/errorPages/NotFound";
import Forbidden from "./components/errorPages/Forbidden";
import InternalError from "./components/errorPages/InternalError";
import UserInfo from './components/UserInfo';
import UserList from "./components/UserList";
import jwt_decode from "jwt-decode";
import {useLocale} from "./components/LoginContext";
import AppUsersPage from "./components/AppUsersPage";
import EditOtherAccount from "./components/EditOtherAccount";
import PasswordResetForm from "./components/PasswordReset/PasswordResetForm";
import EmailConfirm from "./components/EmailConfirmation/EmailConfirm";
import EditOwnAccount from './components/EditOwnAccount';
import AccountActivate from "./components/EmailConfirmation/AccountActivate";
import { GuardProvider, GuardedRoute } from 'react-router-guards';

library.add(fab, faSignInAlt, faUserPlus);

function App() {

    const {token, currentRole, setCurrentRole, setUsername} = useLocale();
    const [roles, setRoles] = useState();
    const GuardProvider = require('react-router-guards').GuardProvider;
    const GuardedRoute = require('react-router-guards').GuardedRoute;

    useEffect(() => {
        if (token) {
            const decodeJwt = jwt_decode(token);
            const roles = decodeJwt['roles'].split(',');
            const login = decodeJwt['sub'];
            setRoles(roles);
            setCurrentRole(roles[0])
            setUsername(login)
            localStorage.setItem('username', login)
        }
    }, [token])

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

    let logged = false
    if(token) logged = true
    else logged = false

    const requireRoles = (to, from, next) => {
        if (to.meta.auth) {
            if (to.meta.logged) {
                if (to.meta.all) {
                    next();
                } else if (to.meta.client) {
                     if (to.meta.currentRole === "CLIENT") {
                        next();
                    } else {
                        next.redirect('/errors/forbidden');
                    }
                } else if (to.meta.manager) {
                    if (to.meta.currentRole === "MANAGER") {
                        next();
                    } else {
                        next.redirect('/errors/forbidden');
                    }
                } else if (to.meta.admin) {
                    if (to.meta.currentRole === "ADMIN") {
                        next();
                    } else {
                        next.redirect('/errors/forbidden');
                    }
                }
            } else {
                next.redirect('/login');
            }
        } else if (!to.meta.auth) {
            next();
        } else {
            next.redirect('/errors/internal');
        }
    };

    return (
        <HashRouter basename={process.env.REACT_APP_ROUTER_BASE || ''}>
            <div className="App">
                <div>
                    <NavigationBar roles={roles} divStyle={divStyle}/>
                    <GuardProvider guards={[requireRoles]} error={NotFound}>
                        <Switch>
                            <GuardedRoute exact path="/" component={Home} meta={{ }}/>
                            <GuardedRoute exact path="/login" component={Login} meta={{ auth: false }}/>
                            <GuardedRoute exact path="/signUp" component={SignUp} meta={{ }}/>
                            <GuardedRoute exact path="/errors/forbidden" component={Forbidden}/>
                            <GuardedRoute exact path="/errors/internal" component={InternalError}/>
                            <GuardedRoute exact path="/login/password-reset" component={PasswordReset} meta={{ auth: false }}/>
                            <GuardedRoute exact path="/confirmedAccount" component={ConfirmedAccount} meta={{ auth: false }}/>
                            <GuardedRoute exact path="/myAccount" component={UserInfo} meta={{ auth: true, all: true, logged, currentRole }}/>
                            <GuardedRoute exact path="/userPage" component={AppUsersPage} meta={{ auth: true, all: true, logged, currentRole }}/>
                            <GuardedRoute exact path="/editOwnAccount" component={EditOwnAccount} meta={{ auth: true, all: true, logged, currentRole }}/>
                            <GuardedRoute exact path="/reset/password/:code" component={PasswordResetForm} meta={{ auth: false }}/>
                            <GuardedRoute exact path="/confirm/email/:code" component={EmailConfirm} meta={{ auth: false }}/>
                            <GuardedRoute exact path="/accounts" component={UserList} meta={{ auth: true, admin: true, logged, currentRole }}/>
                            <Route component={NotFound}/>
                        </Switch>
                    </GuardProvider>
                    <Footer roles={roles} divStyle={divStyle}/>
                </div>
            </div>
        </HashRouter>
    );
}

export default App;
