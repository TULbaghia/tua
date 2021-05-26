import React from 'react';
import {ThemeColorAllowed, useDispatchThemeColor, useThemeColor} from "./ThemeColorProvider";
import {Nav} from "react-bootstrap";
import i18n from "../../../i18n";

function ThemeColorSwitcher(props) {
    const dispatchThemeColor = useDispatchThemeColor();
    const themeColor = useThemeColor();

    const reverseColor = (color) => {
        return color === ThemeColorAllowed.DARK ? ThemeColorAllowed.LIGHT : ThemeColorAllowed.DARK;
    }

    const changeColor = () => {
        dispatchThemeColor(reverseColor(themeColor));
    }

    return (
        <Nav>
            <span onClick={changeColor}>
                <Nav.Link>{i18n.t("theme.color." + reverseColor(themeColor))}</Nav.Link>
            </span>
        </Nav>
    );
}

export default ThemeColorSwitcher;