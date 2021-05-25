import React, {createContext, useContext, useReducer} from 'react';
import './ThemeColorWrapper.scss';

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

    return (color) => {
        dispatch.dispatch(color);
    }
};

export const useThemeColor = () => {
    const dispatch = useContext(ThemeColor);

    return dispatch.themeColor;
};

export default ThemeColorProvider;