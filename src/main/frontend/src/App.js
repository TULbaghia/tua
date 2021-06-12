import React, {useEffect, useState} from 'react';
import { HashRouter, Route, Switch } from 'react-router-dom';
import './App.css';
import NavigationBar from "./components/Partial/Navbar";
import {library} from "@fortawesome/fontawesome-svg-core";
import {fab} from "@fortawesome/free-brands-svg-icons";
import {faSignInAlt, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import Home from "./components/Home";
import Login from "./components/Login/Login";
import SignUp from "./components/SignUp";
import Footer from "./components/Partial/Footer";
import ConfirmedAccount from "./components/ConfirmedAccount";
import PasswordReset from "./components/PasswordReset";
import NotFound from "./components/ErrorPages/NotFound";
import Forbidden from "./components/ErrorPages/Forbidden";
import InternalError from "./components/ErrorPages/InternalError";
import UserInfo from './components/UserInfo';
import UserList from "./components/UserList";
import jwt_decode from "jwt-decode";
import {useLocale} from "./components/LoginContext";
import AppUsersPage from "./components/AppUsersPage";
import EditOtherAccountForm from "./components/EditOtherAccount/EditOtherAccountForm";
import PasswordResetForm from "./components/PasswordReset/PasswordResetForm";
import EmailConfirm from "./components/EmailConfirmation/EmailConfirm";
import EditOwnAccount from './components/EditOwnAccount/EditOwnAccount';
import AccountActivate from "./components/EmailConfirmation/AccountActivate";
import {rolesConstant} from "./Constants";
import { GuardProvider, GuardedRoute } from 'react-router-guards';
import OtherUserInfo from "./components/OtherUserInfo";
import CityList from "./components/city/CitiesList";
import BookingForm from './components/bookings/BookingForm';

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
            if(localStorage.getItem('currentRole') === null) {
                setCurrentRole(roles[0])
            }
            setUsername(login)
            localStorage.setItem('username', login)
        }
    }, [token])

    const divStyle = () => {
        switch (currentRole) {
            case rolesConstant.admin:
                return {backgroundColor: "var(--admin-color)"};
            case rolesConstant.manager:
                return {backgroundColor: "var(--manager-color)"};
            case rolesConstant.client:
                return {backgroundColor: "var(--client-color)"};
        }
    };

    let logged = !!token;

    const requireRoles = (to, from, next) => {
        if(to.meta.logged !== undefined && to.meta.logged !== null && to.meta.logged === true && to.meta.auth === false) {
            next.redirect('/errors/forbidden');
            return;
        }
        if (to.meta.auth) {
            if (to.meta.logged) {
                if (to.meta.all) {
                    next();
                } else if (to.meta.client) {
                     if (to.meta.currentRole === rolesConstant.client) {
                        next();
                    } else {
                        next.redirect('/errors/forbidden');
                    }
                } else if (to.meta.manager) {
                    if (to.meta.currentRole === rolesConstant.manager) {
                        next();
                    } else {
                        next.redirect('/errors/forbidden');
                    }
                } else if (to.meta.admin) {
                    if (to.meta.currentRole === rolesConstant.admin) {
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
                            <GuardedRoute exact path="/login" component={Login} meta={{ auth: false, logged }}/>
                            <GuardedRoute exact path="/login/password-reset" component={PasswordReset} meta={{ auth: false, logged }}/>
                            <GuardedRoute exact path="/confirmedAccount" component={ConfirmedAccount} meta={{ auth: false, logged }}/>
                            <GuardedRoute exact path="/reset/password/:code" component={PasswordResetForm} meta={{ auth: false, logged }}/>
                            <GuardedRoute exact path="/activate/account/:code" component={AccountActivate} meta={{ auth: false, logged }}/>
                            <GuardedRoute exact path="/signUp" component={SignUp} meta={{ auth: false, logged }}/>
                            <GuardedRoute exact path="/confirm/email/:code" component={EmailConfirm} meta={{ auth: false }}/>
                            <GuardedRoute exact path="/errors/forbidden" component={Forbidden}/>
                            <GuardedRoute exact path="/errors/internal" component={InternalError}/>
                            <GuardedRoute exact path="/myAccount" component={UserInfo} meta={{ auth: true, all: true, logged, currentRole }}/>
                            <GuardedRoute exact path="/userPage" component={AppUsersPage} meta={{ auth: true, all: true, logged, currentRole }}/>
                            <GuardedRoute exact path="/editOwnAccount" component={EditOwnAccount} meta={{ auth: true, all: true, logged, currentRole }}/>
                            <GuardedRoute exact path="/accounts" component={UserList} meta={{ auth: true, admin: true, logged, currentRole }}/>
                            <GuardedRoute exact path="/editOtherAccount" component={EditOtherAccountForm} meta={{ auth: true, admin: true, logged, currentRole }}/>
                            <GuardedRoute exact path="/accounts/userInfo" component={OtherUserInfo} meta={{auth: true, admin: true, logged, currentRole }}/>
                            <GuardedRoute exact path="/cities" component={CityList} meta={{auth: true, admin: true, manager: true, logged, currentRole }}/>
                            <GuardedRoute exact path="/booking/add" component={BookingForm} meta={{auth: true, client: true, logged, currentRole }}/>
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
