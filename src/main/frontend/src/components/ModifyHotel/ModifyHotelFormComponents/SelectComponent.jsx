import React from "react";
import '../ModifyHotelWrapper.scss';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import {ErrorMessage, Field} from "formik";

const selectPredicate = (values, predicate) => (
    values.filter(predicate).map(x => <option value={x.id}>{x.name}</option>)
)

export const SelectComponent = ({values, label, name, handleChange, entryValue}) => (
    <>
        <h6 className="mt-3 mb-0">{label}</h6>
        <div className="d-flex w-100">
            <Field className="custom-select my-2 form-control" as="select"
                   name={name}
                   onChange={handleChange}>
                {selectPredicate(values, y => y.name === entryValue)}
                {selectPredicate(values, y => y.name !== entryValue)}
            </Field>
            <span className="custom-asterisk">*</span>
        </div>
        <ErrorMessage name={name}>
            {msg => <div className="err-msg">{msg}</div>}
        </ErrorMessage>
    </>
)

export default SelectComponent;