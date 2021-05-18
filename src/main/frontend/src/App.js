import React, {Component, useState} from 'react';
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
import NotFound from "./components/errorPages/NotFound";
import Forbidden from "./components/errorPages/Forbidden";
import InternalError from "./components/errorPages/InternalError";

library.add(fab, faSignInAlt, faUserPlus);

class App extends Component {
    render() {
        return (
            <div className="App">
                <Router basename={process.env.REACT_APP_ROUTER_BASE || ''}>
                    <div>
                        <NavigationBar />
                        <Switch>
                            <Route exact path="/" component={Home}/>
                            <Route path="/blog" component={BlogScreen}/>
                            <Route path="/login" component={Login}/>
                            <Route path="/pong" component={PingPong}/>
                            <Route path="/errors/forbidden" component={Forbidden}/>
                            <Route path="/errors/internal" component={InternalError}/>

                            <Route component={NotFound}/>
                        </Switch>
                    </div>
                </Router>
            </div>
        );
    }
}


export default App;
