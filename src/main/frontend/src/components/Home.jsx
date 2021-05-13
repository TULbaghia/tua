import React, {Component} from "react";
import logo from "../logo.svg";

class Home extends Component {
    render() {
        return (
            <div>
                <img src={logo} className="App-logo" alt="logo"/>
                <h2>Welcome to React</h2>
            </div>
        );
    }
}

export default Home;