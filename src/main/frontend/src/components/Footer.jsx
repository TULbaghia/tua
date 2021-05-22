import {useEffect, useState} from "react";
import {Dropdown, DropdownButton} from "react-bootstrap";
import {withNamespaces} from "react-i18next";
import userIcon from "../assets/userRole.svg"
import {useLocale} from "./LoginContext";
import "../css/Footer.css"
import DropdownMenu from "react-bootstrap/DropdownMenu";
import DropdownToggle from "react-bootstrap/DropdownToggle";

function AccessLevelSwitcher(props) {

    const {t} = props;

    const {setCurrentRole} = useLocale();
    const [levels, setLevels] = useState([]);

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
            <img alt="userIcon" src={userIcon} style={{marginRight: "1rem"}}/>
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
    const {t} = props;
    const {token} = useLocale();
    const {roles, divStyle} = props;

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