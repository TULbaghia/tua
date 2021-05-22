import React, {Component} from 'react';
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
import PasswordResetForm from "./components/passwordReset/PasswordResetForm";
import NotificationProvider from "./components/Notification/NotificationProvider";

library.add(fab, faSignInAlt, faUserPlus);

class App extends Component {
    render() {
        return (
            <div className="App">
                <NotificationProvider>
                    <Router basename={process.env.REACT_APP_ROUTER_BASE || ''}>
                        <div>
                            <NavigationBar/>
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
                                <Route path="/home" component={UserInfo}/>
                                <Route path="/reset/password/:code" component={PasswordResetForm}/>
                                <Route component={NotFound}/>
                            </Switch>
                            <Footer/>
                        </div>
                    </Router>
                </NotificationProvider>
            </div>
        );
    }
}


export default App;
