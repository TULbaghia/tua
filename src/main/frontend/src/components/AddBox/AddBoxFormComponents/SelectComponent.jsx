import React from "react";
import '../AddBoxFormWrapper.scss';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import {ErrorMessage, Field} from "formik";
import i18n from "i18next";

const selectPredicate = (values, predicate) => (
    values.filter(predicate).map(x => <option value={x}>{i18n.t(x)}</option>)
)

export const SelectComponent = ({values, label, name, handleChange, entryValue}) => (
    <>
        <h6 className="mt-3 mb-0">{label}</h6>
        <div className="d-flex w-100">
            <Field className="custom-select my-2 form-control" as="select"
                   name={name}
                   onChange={handleChange}>
                {selectPredicate(values, y => y === entryValue)}
                {selectPredicate(values, y => y !== entryValue)}
            </Field>
            <span className="custom-asterisk">*</span>
        </div>
        <ErrorMessage name={name}>
            {msg => <div className="err-msg">{msg}</div>}
        </ErrorMessage>
    </>
)

export default SelectComponent;