import React from "react";
import {Row} from "react-bootstrap";

function BreadCrumb(props) {
    return (
        <Row>
            <nav aria-label="breadcrumb" className={"w-100"} style={{}}>
                <ol className="breadcrumb" style={{
                    backgroundColor: "var(--breadcrumb-light)",
                    padding: "0.4rem",
                    borderRadius: "0 0 0.3rem 0.3rem"
                }}>
                    {props.children}
                </ol>
            </nav>
        </Row>
    )
}

export default BreadCrumb;