import i18n from "i18next";
import { reactI18nextModule } from "react-i18next";
import detector from "i18next-browser-languagedetector";

import translationEN from "./locales/en/translation.json";
import translationPL from "./locales/pl/translation.json";

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

export default i18n;
