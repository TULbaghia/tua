import React, {useState} from "react";
import logo from "../logo.svg";
import AuthService from "../AuthService"
import {useHistory} from "react-router";

const Login = () => {

    const history = useHistory();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = e => {
        e.preventDefault()
        AuthService.authorize(email, password)
        localStorage.setItem("isLogged", "true");
        history.push("/")
    }

    const handleShowToken = () => {
        console.log(AuthService.token)
    }

    return (
        <div className="text-center">
            <form className="form-signin" onSubmit={handleLogin}>
                <img className="mb-4" src={logo} alt="" width="72" height="72"/>
                <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
                <input
                    type="email"
                    id="inputEmail"
                    className="form-control"
                    placeholder="Email address"
                    required
                    autoFocus={true}
                    onChange={event => setEmail(event.target.value)}
                />
                <input
                    type="password"
                    id="inputPassword"
                    className="form-control"
                    placeholder="Password"
                    required
                    onChange={event => setPassword(event.target.value)}
                />
                <button className="btn btn-lg btn-primary btn-block" type="submit">
                    Sign in
                </button>
            </form>
        </div>
    );

}

export default Login;
