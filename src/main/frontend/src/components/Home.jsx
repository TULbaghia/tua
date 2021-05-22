import React, {Component} from "react";
import dog from "../images/dog_gradient.jpg";
import logo from "../images/logo.png"
import hand from "../images/hand.jpg"
import cat from "../images/cat.png"
import {withNamespaces} from "react-i18next";
import BreadCrumb from "./BreadCrumb";
import {Link} from "react-router-dom";
import "../css/Home.css"

function Home(props) {
    const {t, i18n} = props
    const {isAuthenticated} = props;

    // *** LANDING PAGE ***
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


    // // *** ADMIN ***
    // return (
    //     <div className="Home">
    //         <BreadCrumb>
    //             <li className="breadcrumb-item">
    //                 <Link to="/">
    //                     <div className="back"> {t('mainPage')} </div>
    //                 </Link>
    //             </li>
    //             <li className="breadcrumb-item active" aria-current="page">{t('adminDashboard')}</li>
    //         </BreadCrumb>
    //         <div>
    //             <div className="greeting">{t('welcome')}, username</div>
    //         </div>
    //     </div>
    // );


    // *** MANAGER ***
    // return (
    //     <div className="Home">
    //         <BreadCrumb>
    //             <li className="breadcrumb-item">
    //                 <Link to="/">
    //                     <div className="back"> {t('mainPage')} </div>
    //                 </Link>
    //             </li>
    //             <li className="breadcrumb-item active" aria-current="page">{t('managerDashboard')}</li>
    //         </BreadCrumb>
    //         <div>
    //             <div className="greeting">{t('welcome')}, username</div>
    //         </div>
    //     </div>
    // );


    // *** USER ***
    // return (
    //     <div className="Home">
    //         <BreadCrumb>
    //             <li className="breadcrumb-item">
    //                 <Link to="/">
    //                     <div className="back"> {t('mainPage')} </div>
    //                 </Link>
    //             </li>
    //             <li className="breadcrumb-item active" aria-current="page">{t('userDashboard')}</li>
    //         </BreadCrumb>
    //         <div>
    //             <div className="greeting">{t('welcome')}, username</div>
    //         </div>
    //     </div>
    // );
}

export default withNamespaces()(Home);