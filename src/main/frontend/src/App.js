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

library.add(fab, faSignInAlt);

class App extends Component {
    render() {
        return (
            <div className="App">
                <Router basename={process.env.REACT_APP_ROUTER_BASE || ''}>
                    <div>
                        <NavigationBar/>
                        {/*<div className="App-header">*/}
                        {/*    <img src={logo} className="App-logo" alt="logo"/>*/}
                        {/*    <h2>Welcome to React</h2>*/}
                        {/*</div>*/}
                        <Switch>
                            <Route exact path="/" component={Home}/>
                            <Route path="/blog" component={BlogScreen}/>
                            <Route path="/about" component={AboutScreen}/>
                        </Switch>
                    </div>
                </Router>
            </div>
        );
    }
}





export default App;
