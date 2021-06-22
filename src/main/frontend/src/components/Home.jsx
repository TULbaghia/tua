import React from "react";
import dog1 from "../images/pies1.jpg";
import cat1 from "../images/kot1.jpg";
import logo from "../images/logo.png"
import hand from "../images/hand.jpg"
import cat from "../images/cat.png"
import {withNamespaces} from "react-i18next";
import {Carousel, Col, Container, Row} from "react-bootstrap";
import {ThemeColorAllowed, useThemeColor} from "./Utils/ThemeColor/ThemeColorProvider";

function Home(props) {
    const {t, i18n} = props
    const {isAuthenticated} = props;

    const themeColor = useThemeColor();
    let bgColor = "#f8f9fa";
    if (themeColor === ThemeColorAllowed.DARK) {
        bgColor = "#424242";
    }

    return (
        <Container fluid className={"darkify pb-4 mb-n2"}>
            <style
                dangerouslySetInnerHTML={{__html: "body {background-color: " + bgColor + "; background-image: unset;"}}/>
            <Row className={"px-0"}>
                <Carousel className={"homeCarousel w-100"}>
                    <Carousel.Item interval={15000}>
                        <img className="img-fluid w-100" src={dog1} alt="First slide"/>
                        <Carousel.Caption className={"d-flex mb-5 align-items-center pb-4 pb-sm-4 pb-md-5"}>
                            <h1 className={"d-flex align-content-center mr-3"}>{t('landingPageTitle')}</h1>
                            <img src={logo} className="" style={{maxHeight: "80px"}} alt={""}/>
                        </Carousel.Caption>
                    </Carousel.Item>
                    <Carousel.Item interval={15000}>
                        <img className="img-fluid w-100" src={cat1} alt="First slide"/>
                        <Carousel.Caption className={"d-flex mb-5 align-items-center pb-4 pb-sm-4 pb-md-5"}>
                            <h1 className={"d-flex align-content-center mr-3"}>{t('landingPageTitle')}</h1>
                            <img src={logo} className="" style={{maxHeight: "80px"}} alt={""}/>
                        </Carousel.Caption>
                    </Carousel.Item>
                </Carousel>
            </Row>
            <Container className={"darkify"}>
                <Row>
                    <Col xs={12} className={"px-0 px-md-3"}>
                        <div className={"darkify pb-3 mb-n2 homeGrid px-0 px-md-3"} style={{minHeight: "500px"}}>
                            <div
                                className={"text-center h3 my-4 d-flex justify-content-center align-items-start textLeft"}>{t('landingPageLeft')}</div>
                            <div
                                className={"text-center h3 my-4 d-flex justify-content-center align-items-start textRight"}>{t('landingPageRight')}</div>
                            <div className={"d-flex justify-content-center align-items-start imageLeft"}>
                                <img alt="cat" className={"img-fluid"} src={cat}/>
                            </div>
                            <div className={"d-flex justify-content-center align-items-start imageRight"}>
                                <img alt="hand" className={"img-fluid"} src={hand}/>
                            </div>
                        </div>
                    </Col>
                </Row>
            </Container>
        </Container>
    );
}

export default withNamespaces()(Home);