import React, {Component, useEffect, useState} from 'react';
import {
    BrowserRouter as Router,
    Route,
    Switch
} from 'react-router-dom';
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
import jwt_decode from "jwt-decode";
import {useLocale} from "./components/LoginContext";
import AppUsersPage from "./components/AppUsersPage";

library.add(fab, faSignInAlt, faUserPlus);

function App() {

    const {token, setToken} = useLocale();
    const [roles, setRoles] = useState();
    const [login, setLogin] = useState();

    useEffect(() => {
        if (token) {
            const decodeJwt = jwt_decode(token);
            const roles = decodeJwt['roles'].split(',');
            const login = decodeJwt['sub'];
            setRoles(roles);
            setLogin(login);
        }
    }, [token])

    return (
        <div className="App">
            <Router basename={process.env.REACT_APP_ROUTER_BASE || ''}>
                <div>
                    <NavigationBar roles={roles} login={login} />
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
                        <Route path="/userpage" component={AppUsersPage}/>
                        <Route component={NotFound}/>
                    </Switch>
                    <Footer roles={roles} login={login} />
                </div>
            </Router>
        </div>
    );
}


export default App;
