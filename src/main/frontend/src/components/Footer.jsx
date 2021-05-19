import React, {Component} from "react";
import "../css/Footer.css"

const style = {
    backgroundColor: "#7749F8",
    textAlign: "center",
    height: "40px",
    width: "100%"
};

const phantom = {
    display: 'block',
    padding: '20px',
    height: '40px',
    width: '100%',
};

function Footer() {
    return (
        <div className="main-footer">
            <div style={phantom} />
            <div style={style}>
                <h4>SSBD 2021 - grupa 6</h4>
            </div>
        </div>
    );
}

export default Footer;