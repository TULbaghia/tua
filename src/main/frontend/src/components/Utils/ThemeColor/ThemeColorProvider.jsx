import React, {createContext, useContext, useReducer} from 'react';
import './ThemeColorWrapper.scss';
import {ResponseErrorHandler} from "../../Validation/ResponseErrorHandler";
import {api} from "../../../Api";
import {useLocale} from "../../LoginContext";
import {
    DispatchNotification,
    useNotificationDangerAndInfinity,
    useNotificationSuccessAndShort
} from "../Notification/NotificationProvider";
import i18n from "../../../i18n";

export const ThemeColorAllowed = {
    LIGHT: "light",
    DARK: "dark"
};

const ThemeColor = createContext();

function ThemeColorProvider({children, ...props}) {
    const [themeColor, setThemeColor] = useReducer((state, action) => {
        switch (action) {
            case ThemeColorAllowed.DARK:
                localStorage.setItem("themeColor", ThemeColorAllowed.DARK)
                return ThemeColorAllowed.DARK;
            case ThemeColorAllowed.LIGHT:
                localStorage.setItem("themeColor", ThemeColorAllowed.LIGHT)
                return ThemeColorAllowed.LIGHT;
            default:
                localStorage.setItem("themeColor", ThemeColorAllowed.LIGHT)
                return ThemeColorAllowed.LIGHT;
        }
    }, localStorage.getItem("themeColor") ?? ThemeColorAllowed.LIGHT);

    return (
        <ThemeColor.Provider value={{themeColor: themeColor, dispatch: setThemeColor}}>
            <div className={["global-theme", "global-theme-" + themeColor].join(" ")}>
                {children}
            </div>
        </ThemeColor.Provider>
    );
}

export const useDispatchThemeColor = () => {
    const dispatch = useContext(ThemeColor);
    const useDanger = useNotificationDangerAndInfinity();
    const useSuccess = useNotificationSuccessAndShort();
    const loginProvider = useLocale();

    return (color) => {
        const token = loginProvider.token;

        if(color === dispatch.themeColor) return;

        if(token) {
            api.showAccountInformation({
                method: "GET",
                headers: {
                    Authorization: token
                }
            }).then((response) => {
                if(response.status === 200 && response.data !== undefined && response.data.themeColor) {
                    if(response.data.themeColor === color.toString().toUpperCase()) {
                        dispatch.dispatch(color);
                        DispatchNotification(useSuccess, {"message": i18n.t("theme.changed.scheme." + color)});
                    } else {
                        api.changeThemeColor(color.toString().toUpperCase(), {
                            method: "PATCH",
                            headers: {
                                "Authorization": token,
                                "If-Match": response.headers.etag
                            }
                        }).then((response) => {
                            if (response.status === 204) {
                                dispatch.dispatch(color);
                                DispatchNotification(useSuccess, {"message": i18n.t("theme.changed.scheme." + color)});
                            }
                        }).catch((e) => {
                            ResponseErrorHandler(e, useDanger);
                        });
                    }
                }
            }).catch((e) => {
                ResponseErrorHandler(e, useDanger);
            });
        } else {
            dispatch.dispatch(color);
            DispatchNotification(useSuccess, {"message": i18n.t("theme.changed.scheme." + color)});
        }
    }
};

export const useDispatchThemeColorAfterLogin = () => {
    const dispatch = useContext(ThemeColor);
    const useDanger = useNotificationDangerAndInfinity();
    const useSuccess = useNotificationSuccessAndShort();

    return (token) => {
        if (token && token.length > 15) {
            api.showAccountInformation({
                method: "GET",
                headers: {
                    Authorization: token
                }
            }).then((response) => {
                if(response.status === 200 && response.data !== undefined && response.data.themeColor) {
                    let color = response.data.themeColor.toString().toLowerCase() === "dark" ? ThemeColorAllowed.DARK : ThemeColorAllowed.LIGHT;
                    dispatch.dispatch(color);
                    DispatchNotification(useSuccess, {"message": i18n.t("theme.changed.scheme." + response.data.themeColor)});
                }
            }).catch((e) => {
                ResponseErrorHandler(e, useDanger);
            });
        }
    }
};

export const useThemeColor = () => {
    const dispatch = useContext(ThemeColor);

    return dispatch.themeColor;
};

export default ThemeColorProvider;