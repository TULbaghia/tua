import React, {Component, useState} from 'react';
import {
    BrowserRouter as Router,
    Route,
    Switch
} from 'react-router-dom';
import './App.css';
import NavigationBar from "./components/navbar";
import {library} from "@fortawesome/fontawesome-svg-core";
import {fab} from "@fortawesome/free-brands-svg-icons";
import {faSignInAlt, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import Home from "./components/home";
import BlogScreen from "./components/blog";
import PingPong from "./components/pingpong";
import Login from "./components/login";

library.add(fab, faSignInAlt, faUserPlus);

class App extends Component {
    state = {
        loggedIn: localStorage.getItem('isLogged')
    }

    render() {
        return (
            <div className="App">
                <Router basename={process.env.REACT_APP_ROUTER_BASE || ''}>
                    <div>
                        <NavigationBar isAuthenticated={this.state.loggedIn}/>
                        <Switch>
                            <Route exact path="/" component={Home}/>
                            <Route path="/blog" component={BlogScreen}/>
                            <Route path="/login" component={Login}/>
                            <Route path="/pong" component={PingPong}/>
                        </Switch>
                    </div>
                </Router>
            </div>
        );
    }
}


export default App;
