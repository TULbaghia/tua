import React, {Component} from "react";
import {Dropdown, DropdownButton} from "react-bootstrap";
import {withNamespaces} from "react-i18next";
import userIcon from "../assets/userRole.svg"
import {useLocale} from "./LoginContext";
import "../css/Footer.css"
import DropdownMenu from "react-bootstrap/DropdownMenu";
import DropdownToggle from "react-bootstrap/DropdownToggle";

const style = {

};

class AccessLevelSwitcher extends React.Component{

    constructor(props) {
        super(props);
        this.t = props.t
    }

    state = {
        levels: ['admin', 'manager', 'user'],
        chosen: 0
    }

    render() {
        return(
            <div style={{display: "flex"}}>
                <img src={userIcon} style={{marginRight: "1rem"}}/>
                <Dropdown>
                    <DropdownToggle id="dropdown-basic" key='up' drop='up' className="roleMenu">
                        {this.t('roleChange')}
                    </DropdownToggle>
                    <DropdownMenu>
                        <Dropdown.Item onClick={()=>alert('Wybrałeś ' + this.state.levels[0])}>{this.t(this.state.levels[0])}</Dropdown.Item>
                        <Dropdown.Item className="item">{this.t(this.state.levels[1])}</Dropdown.Item>
                        <Dropdown.Item className="item">{this.t(this.state.levels[2])}</Dropdown.Item>
                    </DropdownMenu>
                </Dropdown>
            </div>
        )
    }
}

function Footer(props) {
    const  {t,i18n} = props
    const {token, setToken} = useLocale();
    return (
            <div className="footer">
                {token !== null && token !== '' ? (
                    <div style={{marginLeft: "10rem", width: "10%"}}>
                        <AccessLevelSwitcher t={t}/>
                    </div>
                ) : null
                }
            </div>
    );
}

export default withNamespaces()(Footer);