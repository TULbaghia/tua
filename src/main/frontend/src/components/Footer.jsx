import React, {Component} from "react";

// // *** LANDING PAGE ***
const style = {
    backgroundColor: "#7749F8",
    textAlign: "center",
    position: "fixed",
    bottom: "0",
    height: "40px",
    width: "100%"
};

// // *** ADMIN ***
// const style = {
//     backgroundColor: "#EF5DA8",
//     textAlign: "center",
//     position: "fixed",
//     bottom: "0",
//     height: "40px",
//     width: "100%"
// };

// *** MANAGER ***
// const style = {
//     backgroundColor: "#F178B6",
//     textAlign: "center",
//     position: "fixed",
//     bottom: "0",
//     height: "40px",
//     width: "100%"
// };

// // *** USER ***
// const style = {
//     backgroundColor: "#EFADCE",
//     textAlign: "center",
//     position: "fixed",
//     bottom: "0",
//     height: "40px",
//     width: "100%"
// };

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