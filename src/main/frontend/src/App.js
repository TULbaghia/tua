import React, {useEffect, useState} from 'react';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import './App.css';
import NavigationBar from "./components/Navbar";
import {library} from "@fortawesome/fontawesome-svg-core";
import {fab} from "@fortawesome/free-brands-svg-icons";
import {faSignInAlt, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import Home from "./components/Home";
import BlogScreen from "./components/Blog";
import PingPong from "./components/PingPong";
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
import NotificationProvider from "./components/Notification/NotificationProvider";
import CriticalOperationProvider from "./components/CriticalOperations/CriticalOperationProvider";
import PasswordResetForm from "./components/PasswordReset/PasswordResetForm";
import EmailConfirm from "./components/EmailConfirmation/EmailConfirm";
import EditOwnAccount from './components/EditOwnAccount';

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
            case 'ADMIN':
                return {backgroundColor: "#EF5DA8"};
            case 'MANAGER':
                return {backgroundColor: "#F178B6"};
            case 'CLIENT':
                return {backgroundColor: "#EFADCE"};
        }
    };

    return (
        <div className="App">
            <NotificationProvider>
                <CriticalOperationProvider>
                    <Router basename={process.env.REACT_APP_ROUTER_BASE || ''}>
                        <div>
                            <NavigationBar roles={roles} divStyle={divStyle}/>
                            <Switch>
                                <Route exact path="/" component={Home}/>
                                <Route path="/blog" component={BlogScreen}/>
                                <Route exact path="/login" component={Login}/>
                                <Route path="/signUp" component={SignUp}/>
                                <Route path="/pong" component={PingPong}/>
                                <Route path="/errors/forbidden" component={Forbidden}/>
                                <Route path="/errors/internal" component={InternalError}/>
                                <Route path="/login/password-reset" component={PasswordReset}/>
                                <Route path="/confirmedAccount" component={ConfirmedAccount}/>
                                <Route path="/myAccount" component={UserInfo}/>
                                <Route path="/userPage" component={AppUsersPage}/>
                                <Route path="/accounts" component={UserList}/>
                                <Route path="/editOwnAccount" component={EditOwnAccount}/>
                                <Route path="/reset/password/:code" component={PasswordResetForm}/>
                                <Route path="/confirm/email/:code" component={EmailConfirm}/>
                                <Route component={NotFound}/>
                            </Switch>
                            <Footer roles={roles} divStyle={divStyle}/>
                        </div>
                    </Router>
                </CriticalOperationProvider>
            </NotificationProvider>
        </div>
    );
}

export default App;
