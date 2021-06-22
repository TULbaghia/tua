import {Dropdown, Nav, Navbar} from "react-bootstrap";
import {LinkContainer} from "react-router-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {Link, useHistory} from "react-router-dom";
import {useLocale} from "../LoginContext";
import {withNamespaces} from 'react-i18next';
import {library} from "@fortawesome/fontawesome-svg-core";
import {faUser} from "@fortawesome/free-solid-svg-icons";
import "../../css/Navbar.scss";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import ThemeColorSwitcher from "../Utils/ThemeColor/ThemeColorSwitcher";
import {rolesConstant} from "../../Constants";
import {useEffect, useState} from "react";
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {useThemeColor} from "../Utils/ThemeColor/ThemeColorProvider";
import {api} from "../../Api"
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";

library.add(faUser);

function LanguageSwitcher(props) {

    const {t, i18n} = props
    const {token, setToken} = useLocale();
    const [etag, setETag] = useState();
    const colorTheme = useThemeColor();

    const langs = ['pl', 'en']

    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchDangerNotification = useNotificationDangerAndInfinity();


    const getEtag = async () => {
        const response = await api.showAccountInformation({
            method: "GET",
            headers: {
                Authorization: token,
            }
        })
        return response.headers.etag;
    };

    useEffect(() => {
        if (token) {
            getEtag().then(r => setETag(r));
        }
    }, [token, i18n.language, colorTheme]);

    const handleClickPl = () => {
        setLanguage(i18n, "pl")
    }

    const handleClickEn = () => {
        setLanguage(i18n, "en")
    }

    const handleClickLoggedPl = () => {
        api.editOwnLanguage("pl", {
            headers: {
                Authorization: token,
                "If-Match": etag
            }
        }).then(() => {
            handleClickPl();
            dispatchNotificationSuccess({message: i18n.t('languageChangeSuccess')})
        }).catch(err => {
            ResponseErrorHandler(err, dispatchDangerNotification);
        })
    }

    const handleClickLoggedEn = () => {
        api.editOwnLanguage("en", {
            headers: {
                Authorization: token,
                "If-Match": etag
            }
        }).then(() => {
            handleClickEn();
            dispatchNotificationSuccess({message: i18n.t('languageChangeSuccess')})
        }).catch(err => {
            ResponseErrorHandler(err, dispatchDangerNotification);
        })
    }

    return (
        <>
            <Dropdown>
                <DropdownToggle id="dropdown-basic" className="dropButton pl-0 pl-lg-2 pr-0 pr-lg-2" variant="Info">
                    <span style={{marginRight: "10px"}}>{i18n.t("language")} [{i18n.language.substring(0, 2).toUpperCase()}]</span>
                </DropdownToggle>

                <Dropdown.Menu>
                    <Dropdown.Item onClick={token !== null && token !== '' ? (
                            handleClickLoggedPl)
                        : (
                            handleClickPl)}>{t(langs[0])}</Dropdown.Item>
                    <Dropdown.Item onClick={token !== null && token !== '' ? (
                            handleClickLoggedEn)
                        : (
                            handleClickEn)}>{t(langs[1])}</Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>
        </>
    )
}

function NavigationBar(props) {
    const {t, divStyle, i18n} = props
    const history = useHistory();
    const {token, username, setToken, currentRole, setCurrentRole, setUsername} = useLocale();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity();
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();

    const handleLogout = () => {

        const requestOptions = {
            method: "GET",
            headers: {
                Authorization: token,
            },
            validateStatus: (status) => {
                return (status >= 200 && status <= 299) || status === 401;
            }
        };

        api.logout(requestOptions)
            .then((res) => {})
            .catch((err) =>  {});
        setToken('');
        setCurrentRole('');
        setUsername('');
        localStorage.removeItem('token')
        localStorage.removeItem('currentRole')
        localStorage.removeItem('username')
        clearTimeout(localStorage.getItem('timeoutId') ?? 0)
        history.push("/login")
        dispatchNotificationSuccess({message: t("logout.success")});
    }

    // *** LANDING PAGE ***
    const {isAuthenticated} = props;
    return (
        <>
            {token !== null && token !== '' ? (
                //------------------------LOGGED USER VIEW----------------------------
                <Navbar expand="lg" className="main-navbar" style={divStyle()}>
                    <Navbar.Brand>
                        <div className="name d-flex flex-wrap justify-content-start align-items-center position-relative mr-3" style={{width: "min-content"}}>
                            <LinkContainer to="/">
                                <h4 className={"cursor-pointer"}>Purrfecti<img src={"/favicon.ico"} className={"img-fluid"} style={{maxHeight: "20px"}}/>n</h4>
                            </LinkContainer>
                            <sub className={"small position-absolute mb-0"} style={{fontSize: ".7rem", bottom: "5px", left: "1px"}}>{t('animalHotel')}</sub>
                        </div>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="mr-auto d-flex align-items-start align-items-lg-center">
                            {/*<LinkContainer to="/">*/}
                            {/*    <Nav.Link>{t('mainPage')}</Nav.Link>*/}
                            {/*</LinkContainer>*/}
                            <LinkContainer to="/hotels">
                                <Nav.Link>{t('hotels')}</Nav.Link>
                            </LinkContainer>
                            {currentRole === rolesConstant.admin && (
                                <>
                                    <LinkContainer to="/accounts">
                                        <Nav.Link>{t('accountList')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/cities">
                                        <Nav.Link>{t('cityList')}</Nav.Link>
                                    </LinkContainer>
                                </>
                            )}
                            {currentRole === rolesConstant.manager && (
                                <>
                                    <Dropdown className="">
                                        <DropdownToggle id="dropdown-basic" className="dropButton pl-0 pl-lg-2 pr-0 pr-lg-2" variant="Info">
                                            <span style={{marginRight: "10px"}}>{t('myHotel')}</span>
                                        </DropdownToggle>

                                        <Dropdown.Menu>
                                            <Dropdown.Item as={Link} to="/hotels/editOwnHotel">
                                                {t('editOwnHotel.navbar.title')}
                                            </Dropdown.Item>
                                            <Dropdown.Item as={Link} to="/hotels/generateReport">
                                                {t('generateReport.navbar.title')}
                                            </Dropdown.Item>
                                            <Dropdown.Item as={Link} to="/boxes/own">
                                                {t("boxList.navbar.title")}
                                            </Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                    <LinkContainer to="/activeReservations">
                                        <Nav.Link>{t('menu.activeReservations')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/archiveReservations">
                                        <Nav.Link>{t('menu.archiveReservations')}</Nav.Link>
                                    </LinkContainer>
                                </>
                            )}
                            {currentRole === rolesConstant.client && (
                                <>
                                    <LinkContainer to="/reservation">
                                        <Nav.Link>{t('reservation')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/activeReservations">
                                        <Nav.Link>{t('menu.activeReservations')}</Nav.Link>
                                    </LinkContainer>
                                    <LinkContainer to="/archiveReservations">
                                        <Nav.Link>{t('menu.archiveReservations')}</Nav.Link>
                                    </LinkContainer>
                                </>
                            )}
                        </Nav>
                        <Nav className="navbar-right d-flex align-items-start align-items-lg-center">
                            <ThemeColorSwitcher/>
                            <LanguageSwitcher t={t} i18n={i18n}/>
                            <Dropdown alignRight={true}>
                                <Dropdown.Toggle id="dropdown-basic" className="dropButton pl-0 pl-lg-2 pr-0 pr-lg-2" variant="Info">
                                    <FontAwesomeIcon icon="user"/>
                                    {' '}{username}{' '}
                                </Dropdown.Toggle>

                                <Dropdown.Menu>
                                    <Dropdown.Item>
                                        <LinkContainer to="/myAccount"><span>{t('myAccount')}</span></LinkContainer>
                                    </Dropdown.Item>
                                    <Dropdown.Item onSelect={handleLogout}>
                                        <LinkContainer to="/">
                                            <span>{t('signOut')}</span>
                                        </LinkContainer>
                                    </Dropdown.Item>
                                </Dropdown.Menu>
                            </Dropdown>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            ) : (
                //------------------------GUEST VIEW----------------------------
                <Navbar expand="lg" className="main-navbar" style={divStyle()}>
                    <Navbar.Brand>
                        <div className="name d-flex flex-wrap justify-content-start align-items-center position-relative mr-3" style={{width: "min-content"}}>
                            <LinkContainer to="/">
                                <h4 className={"cursor-pointer"}>Purrfecti<img src={"/favicon.ico"} className={"img-fluid"} style={{maxHeight: "20px"}}/>n</h4>
                            </LinkContainer>
                            <sub className={"small position-absolute mb-0"} style={{fontSize: ".7rem", bottom: "5px", left: "1px"}}>{t('animalHotel')}</sub>
                        </div>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="mr-auto d-flex align-items-start align-items-lg-center">
                            <LinkContainer to="/">
                                <Nav.Link>{t('mainPage')}</Nav.Link>
                            </LinkContainer>
                            <LinkContainer to="/hotels">
                                <Nav.Link>{t('hotels')}</Nav.Link>
                            </LinkContainer>
                        </Nav>
                        <Nav className="navbar-right d-flex align-items-start align-items-lg-center">
                            <ThemeColorSwitcher/>
                            <LanguageSwitcher t={t} i18n={i18n}/>
                            <div className={"d-flex flex-nowrap flex-md-wrap mt-2 mt-lg-0 mb-2 mb-lg-0"}>
                                <LinkContainer to="/signUp">
                                    <Nav.Link className="signUp ml-0 ml-lg-2">
                                        <FontAwesomeIcon className="signUpIcon" icon="user-plus"/>
                                        <div className="signUpText">{t('signUp')}</div>
                                    </Nav.Link>
                                </LinkContainer>
                                <LinkContainer to="/login">
                                    <Nav.Link className="login">
                                        <FontAwesomeIcon className="loginIcon" icon="sign-in-alt"/>
                                        <div className="loginText">{t('signIn')}</div>
                                    </Nav.Link>
                                </LinkContainer>
                            </div>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            )}
        </>
    )
}

export function setLanguage(i18n, lang) {
    i18n.changeLanguage(lang)
}

export function getUserLanguage(token, i18n, showNotification = (() => {
}), showDanger = (() => {})) {
    if (token !== null && token !== '') {
        api.showAccountInformation({
            headers: {
                Authorization: token
            }
        }).then(res => {
            setLanguage(i18n, res.data.language);
            showNotification();
        }).catch(err => ResponseErrorHandler(err, showDanger))
    } else setLanguage(i18n, "en")
}

export default withNamespaces()(NavigationBar);