import React from "react";
import {withNamespaces} from "react-i18next";
import {useHistory} from "react-router";
import {Col, Container, Row} from "react-bootstrap";


function ConfirmedAccount(props) {
    const {t, i18n} = props
    const history = useHistory();

    return (
        <Container fluid>
            <div className="container mt-3">
                <Row>
                    <Col xs={12} sm={8} md={7} lg={6} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div>
                            <h1 className={"px-0 px-md-3 h3"}>{t('congratulations')}</h1>
                            <p className={"px-0 px-md-3"} style={{fontSize: 16}}>{t('accountConfirmedMsg')}</p>
                            <section className="button-section d-flex flex-wrap justify-content-around mt-3"
                                     style={{display: "inline-block"}}>
                                <button className="btn btn-primary mt-2" onClick={() => history.push("/")}
                                        style={{backgroundColor: "#7749F8"}}>
                                    {t('mainPage')}
                                </button>
                                <button className="btn btn-primary mt-2" onClick={() => history.push("/login")}
                                        style={{backgroundColor: "#7749F8"}}>
                                    {t('signIn')}
                                </button>
                            </section>
                        </div>
                    </Col>
                </Row>
            </div>
        </Container>
    );

}

export default withNamespaces()(ConfirmedAccount);