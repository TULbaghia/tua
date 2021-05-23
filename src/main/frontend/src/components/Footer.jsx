import React, {Component, useEffect, useState} from "react";
import {Dropdown, DropdownButton} from "react-bootstrap";
import {withNamespaces} from "react-i18next";
import userIcon from "../assets/userRole.svg"
import {useLocale} from "./LoginContext";
import "../css/Footer.css"
import DropdownMenu from "react-bootstrap/DropdownMenu";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import jwt_decode from "jwt-decode";

function AccessLevelSwitcher(props) {

    const {t, i18n} = props;

    const {setCurrentRole} = useLocale();
    const [levels, setLevels] = useState([]);
    const [chosen, setChosen] = useState(0);


    useEffect(() => {
        if (props.levels) {
            setLevels(props.levels)
        }
    }, [props.levels])

    const handleSelect=(e)=> {
        console.log(e);
        setCurrentRole(e);
        localStorage.setItem('currentRole', e)
    }

    return (
        <div style={{display: "flex"}}>
            <img src={userIcon} style={{marginRight: "1rem"}}/>
            <Dropdown onSelect={handleSelect}>
                <DropdownToggle id="dropdown-basic" key='up' drop='up' className="roleMenu">
                    {t('roleChange')}
                </DropdownToggle>
                <DropdownMenu>
                    {levels.map((level) => (
                        <Dropdown.Item eventKey={level} className="item">{t(level)}</Dropdown.Item>
                    ))}
                </DropdownMenu>
            </Dropdown>
        </div>
    )
}

function Footer(props) {
    const {t, i18n} = props;
    const {token, setToken} = useLocale();
    const {roles} = props;

    const {currentRole} = useLocale();

    const divStyle = () => {
        switch (currentRole) {
            case 'ADMIN':
                return {backgroundColor: "#EF5DA8"};
            case 'MANAGER':
                return {backgroundColor: "#F178B6"};
            case 'CLIENT':
                return {backgroundColor: "#EFADCE"};
            default:
                return {backgroundColor: "#7749f8"}
        }
    };

    return (
        <div className="footer" style={ divStyle() }>
            {token !== null && token !== '' ? (
                <div style={{marginLeft: "10rem", width: "10%"}}>
                    <AccessLevelSwitcher t={t} levels={roles}/>
                </div>
            ) : null
            }
        </div>
    );
}

export default withNamespaces()(Footer);