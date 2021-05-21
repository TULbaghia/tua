import React from "react";

function BreadCrumb(props) {
    return(
        <nav aria-label="breadcrumb" style={{marginRight: "calc(50% - 50vw)", marginLeft: "calc(50% - 50vw)"}}>
            <ol className="breadcrumb" style={{backgroundColor: "#f8f9fa", padding: "0.4rem", borderRadius: "0 0 0.3rem 0.3rem"}}>
                {props.children}
            </ol>
        </nav>
    )
}

export default BreadCrumb;