import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import "./i18n";
import App from "./App";
import NotificationProvider from "./components/Utils/Notification/NotificationProvider";
import CriticalOperationProvider from "./components/Utils/CriticalOperations/CriticalOperationProvider";
import reportWebVitals from "./reportWebVitals";
import {LoginProvider} from "./components/LoginContext";
import ThemeColorProvider from "./components/Utils/ThemeColor/ThemeColorProvider";

ReactDOM.render(
    <React.StrictMode>
        <ThemeColorProvider>
            <NotificationProvider>
                <CriticalOperationProvider>
                    <LoginProvider>
                        <App/>
                    </LoginProvider>
                </CriticalOperationProvider>
            </NotificationProvider>
        </ThemeColorProvider>
    </React.StrictMode>,
    document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
