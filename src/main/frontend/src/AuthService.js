class AuthService {
    static token = '';

    static setToken(token) {
        AuthService.token = token;
    }

    static authorize(login, password) {
        const requestOptions = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ login: login, password: password }),
        };

        fetch('/resources/auth/auth', requestOptions)
            .then((res) => res.text())
            .then((token) => {
                const tokenBearer = 'Bearer ' + token;
                AuthService.setToken(tokenBearer);
            })
    }
}

export default AuthService;