import React, {Component} from "react";
import dog from "../images/dog_gradient.jpg";
import logo from "../images/logo.png"
import hand from "../images/hand.jpg"
import cat from "../images/cat.png"
import {withNamespaces} from "react-i18next";
import "../css/Home.css"

function Home(props) {
    const {t, i18n} = props
    const {isAuthenticated} = props;

    return (
        <div className="Home">
            <div className="row">
                <div className="column">
                    <div className="top">
                        <div className="photo">
                            <img alt="dog" className="dog" src={dog} />
                            <h2 className="title">{t('landingPageTitle')}</h2>
                            <img src={logo} className="logo"/>
                        </div>
                      </div>
                    <div className="row">
                        <div className="bottom">
                            <div className="columnLeft">
                                <div className="textLeft">{t('landingPageLeft')}</div>
                                <img alt="cat" className="cat" src={cat} />
                            </div>
                            <div className="columnRight">
                                <div className="textRight">{t('landingPageRight')}</div>
                                <img alt="hand" className="hand" src={hand} />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default withNamespaces()(Home);