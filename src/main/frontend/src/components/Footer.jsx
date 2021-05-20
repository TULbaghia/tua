import React, {Component} from "react";

const style = {
    backgroundColor: "#7749F8",
    textAlign: "center",
    position: "absolute",
    bottom: "0",
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
        <div>
            <div style={phantom} />
            <div style={style}>
                <h4>SSBD 2021 - grupa 6</h4>
            </div>
        </div>
    );
}

export default Footer;