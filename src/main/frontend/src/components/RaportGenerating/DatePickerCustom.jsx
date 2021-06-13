import React from 'react';
import i18n from "../../i18n";
import DatePicker from "react-datepicker";
import pl from "date-fns/locale/pl";
import enUs from "date-fns/locale/en-US";

function DatePickerCustom({pickDate, setPickDate, label}) {
    return (
        <div className="date-picker-custom">
            <h5>{label}</h5>
            <DatePicker locale={i18n.language === "pl" ? pl : enUs} selected={pickDate}
                        onChange={(date) => setPickDate(date)}
                        showTimeSelect
                        timeCaption={i18n.t("reportGenerator.time_label")}/>
        </div>
    );
}

export default DatePickerCustom;