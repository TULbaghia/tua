import React, {useEffect, useState} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
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
import EditOwnAccount from './components/EditOwnAccount';
import AccountActivate from "./components/EmailConfirmation/AccountActivate";
import {rolesConstant} from "./Constants";

library.add(fab, faSignInAlt, faUserPlus);

function App() {

    const {token, currentRole, setCurrentRole, setUsername} = useLocale();
    const [roles, setRoles] = useState();

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
            case rolesConstant.admin:
                return {backgroundColor: "var(--admin-color)"};
            case rolesConstant.manager:
                return {backgroundColor: "var(--manager-color)"};
            case rolesConstant.client:
                return {backgroundColor: "var(--client-color)"};
        }
    };

    return (
        <div className="App">
            <Router basename={process.env.REACT_APP_ROUTER_BASE || ''}>
                <div>
                    <NavigationBar roles={roles} divStyle={divStyle}/>
                    <Switch>
                        <Route exact path="/" component={Home}/>
                        <Route exact path="/login" component={Login}/>
                        <Route exact path="/signUp" component={SignUp}/>
                        <Route exact path="/errors/forbidden" component={Forbidden}/>
                        <Route exact path="/errors/internal" component={InternalError}/>
                        <Route exact path="/login/password-reset" component={PasswordReset}/>
                        <Route exact path="/confirmedAccount" component={ConfirmedAccount}/>
                        <Route exact path="/myAccount" component={UserInfo}/>
                        <Route exact path="/userPage" component={AppUsersPage}/>
                        <Route exact path="/accounts" component={UserList}/>
                        <Route exact path="/editOtherAccount" component={EditOtherAccountForm}/>
                        <Route exact path="/editOwnAccount" component={EditOwnAccount}/>
                        <Route exact path="/reset/password/:code" component={PasswordResetForm}/>
                        <Route exact path="/confirm/email/:code" component={EmailConfirm}/>
                        <Route exact path="/activate/account/:code" component={AccountActivate}/>
                        <Route component={NotFound}/>
                    </Switch>
                    <Footer roles={roles} divStyle={divStyle}/>
                </div>
            </Router>
        </div>
    );
}

export default App;
