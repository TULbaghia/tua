import React, {Component} from 'react';
import {
    BrowserRouter as Router,
    Route,
    Switch
} from 'react-router-dom';
import './App.css';
import NavigationBar from "./components/navbar";
import { library } from "@fortawesome/fontawesome-svg-core";
import { fab } from "@fortawesome/free-brands-svg-icons";
import { faSignInAlt } from "@fortawesome/free-solid-svg-icons";
import Home from "./components/home";
import BlogScreen from "./components/blog";
import AboutScreen from "./components/about";
import PingPong from "./components/pingpong";

library.add(fab, faSignInAlt);

class App extends Component {
    render() {
        return (
            <div className="App">
                <Router basename={process.env.REACT_APP_ROUTER_BASE || ''}>
                    <div>
                        <NavigationBar/>
                        <Switch>
                            <Route exact path="/" component={Home}/>
                            <Route path="/blog" component={BlogScreen}/>
                            <Route path="/about" component={AboutScreen}/>
                            <Route path="/pong" component={PingPong}/>
                        </Switch>
                    </div>
                </Router>
            </div>
        );
    }
}





export default App;
