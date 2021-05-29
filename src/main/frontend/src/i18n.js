import i18n from "i18next";
import { reactI18nextModule } from "react-i18next";
import detector from "i18next-browser-languagedetector";

import translationEN from "./locales/en/translation.json";
import translationPL from "./locales/pl/translation.json";
import {enDays, enMonths} from "./locales/en/date";
import {plDays, plMonths} from "./locales/pl/date";

const resources = {
  pl: {
    translation: translationPL,
  },
  en: {
    translation: translationEN,
  },
};

i18n
.use(reactI18nextModule)
.use(detector)
  .init({
    resources,
    fallbackLng: 'en',
    // lng: "pl",

    // keySeparator: false,
    interpolation: {
      escapeValue: false,
    },
  });

export const dateConverter = (date) =>{
    debugger
    const parsedDate = new Date(date)
    const day = parsedDate.getDate()
    const year = parsedDate.getFullYear()
    let hours = parsedDate.getHours()
    let minutes = parsedDate.getMinutes()
    let seconds = parsedDate.getSeconds()

    if (hours < 10) hours = `0${hours}`
    if (minutes < 10) minutes = `0${minutes}`
    if (seconds < 10) seconds = `0${seconds}`

    const time = `${hours}:${minutes}:${seconds}`
    if (i18n.language === 'pl'){
        const month = plMonths[parsedDate.getMonth()]
        const weekDay = plDays[parsedDate.getDay()]
        return `${weekDay}, ${day} ${month} ${year} ${time}`
    }
    else {
        const month = enMonths[parsedDate.getMonth()]
        const weekDay = enDays[parsedDate.getDay()]
        return `${weekDay}, ${day} ${month} ${year} ${time}`
    }
}

export default i18n;
