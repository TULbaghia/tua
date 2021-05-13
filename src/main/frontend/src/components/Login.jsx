import React, {useState} from "react";
import logo from "../logo.svg";
import {useHistory} from "react-router";
import {useLocale} from "./LoginContext";

const Login = () => {

    const history = useHistory();
    const { token, setToken } = useLocale();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = e => {
        e.preventDefault()
        history.push("/")

        const requestOptions = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ login: email, password: password }),
        };

        fetch('/resources/auth/auth', requestOptions)
            .then((res) => {
                if(res.status !== 202) {
                    throw Error('Invalid credentials')
                }
                return res.text()
            })
            .then((token) => {
                const tokenBearer = 'Bearer ' + token;
                setToken(tokenBearer);
                localStorage.setItem('token', tokenBearer)
            })
            .catch(err => {
                console.log(err.message)
            })
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
