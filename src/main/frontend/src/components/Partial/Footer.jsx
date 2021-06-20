import {useEffect, useState} from "react";
import {Col, Dropdown} from "react-bootstrap";
import {withNamespaces} from "react-i18next";
import userIcon from "../../assets/userRole.svg"
import {useLocale} from "../LoginContext";
import "../../css/Footer.css"
import DropdownMenu from "react-bootstrap/DropdownMenu";
import DropdownToggle from "react-bootstrap/DropdownToggle";
import i18n from '../../i18n';
import {
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Utils/Notification/NotificationProvider";
import {rolesConstant} from "../../Constants";
import {useHistory} from "react-router-dom";
import {api} from "../../Api"
import {ResponseErrorHandler} from "../Validation/ResponseErrorHandler";

function AccessLevelSwitcher(props) {

    const {setCurrentRole, token, currentRole} = useLocale();
    const [levels, setLevels] = useState([]);
    const dispatchNotificationSuccess = useNotificationSuccessAndShort();
    const dispatchNotificationDanger = useNotificationDangerAndInfinity()

    const history = useHistory();

    useEffect(() => {
        if (props.levels) {
            setLevels(props.levels)
        }
    }, [props.levels])

    const handleSelect=(level)=> {
        setCurrentRole(level);
        localStorage.setItem('currentRole', level);
        if(level !== rolesConstant.client && level!== currentRole) {
            handleChangeLevel(level);
            history.push("/")
        }
    }

    const handleChangeLevel = (e) => {
        const requestOptions = {
            method: "GET",
            headers: {
                Authorization: token,
            }
        };
        api.changeOwnAccessLevel(e, requestOptions)
            .then(()=>dispatchNotificationSuccess({message: i18n.t('roleChanged') + i18n.t(e)}))
            .catch((err)=> ResponseErrorHandler(err, dispatchNotificationDanger))
    }

    return (
        <div className={"d-flex ml-3"}>
            <img alt="userIcon" src={userIcon} className={"mr-2"}/>
            <Dropdown onSelect={handleSelect}>
                <DropdownToggle id="dropdown-basic" key='up' drop='up' className="roleMenu text-white" style={{backgroundColor: "transparent"}}>
                    {i18n.t('roleChange')}
                </DropdownToggle>
                <DropdownMenu>
                    {levels.map((level) => (
                        <Dropdown.Item eventKey={level} className="item">{i18n.t(level)}</Dropdown.Item>
                    ))}
                </DropdownMenu>
            </Dropdown>
        </div>
    )
}

function Footer(props) {
    const {token, currentRole} = useLocale();
    const {roles, divStyle} = props;

    return (
        <div className={"container-fluid footer position-fixed b-0 d-flex bottom-navbar"} style={ divStyle() }>
            <div className={"row"}>
                <Col xs={12} className={"d-flex align-items-center flex-wrap flex-row"}>
                    <div>
                        {token !== null && token !== '' ? <div><AccessLevelSwitcher levels={roles}/></div> : ""}
                    </div>
                    <div className={"text-white d-none d-sm-flex"}>
                        {token !== null && token !== '' ? <span className={"px-2"}>|</span> : ""}
                        &copy; SSBD202106
                    </div>
                </Col>
            </div>
        </div>
    );
}

export default withNamespaces()(Footer);