import React from "react";
import {Row} from "react-bootstrap";

function BreadCrumb(props) {
    return (
        <Row>
            <nav aria-label="breadcrumb" className={"w-100"} style={{}}>
                <ol className="breadcrumb" style={{
                    backgroundColor: "var(--breadcrumb-light)",
                    padding: "0.4rem 1rem",
                    borderRadius: "0"
                }}>
                    {props.children}
                </ol>
            </nav>
        </Row>
    )
}

export default BreadCrumb;