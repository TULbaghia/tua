import React from "react";
import dog1 from "../images/pies1.jpg";
import cat1 from "../images/kot1.jpg";
import logo from "../images/logo.png"
import hand from "../images/hand.jpg"
import cat from "../images/cat.png"
import {withNamespaces} from "react-i18next";
import {Carousel, Container, Row} from "react-bootstrap";

function Home(props) {
    const {t, i18n} = props
    const {isAuthenticated} = props;

    return (
        <Container fluid>
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
            <Row>
                <div className={"darkify pb-3 mb-n2 homeGrid px-3"} style={{minHeight: "500px"}}>
                    <div className={"text-center h3 my-4 d-flex justify-content-center align-items-start textLeft"}>{t('landingPageLeft')}</div>
                    <div className={"text-center h3 my-4 d-flex justify-content-center align-items-start textRight"}>{t('landingPageRight')}</div>
                    <div className={"d-flex justify-content-center align-items-start imageLeft"}>
                        <img alt="cat" className={"img-fluid"} src={cat}/>
                    </div>
                    <div className={"d-flex justify-content-center align-items-start imageRight"}>
                        <img alt="hand" className={"img-fluid"} src={hand}/>
                    </div>
                </div>
            </Row>
        </Container>
    );
}

export default withNamespaces()(Home);