import React from "react";
import '../ModifyHotelWrapper.scss';
import 'moment/locale/pl';
import 'moment/locale/en-gb';
import {ErrorMessage, Field} from "formik";

export const SelectComponent = ({values, name, handleChange, placeholder}) => (
    <>
        <div className="d-flex w-100">
            <Field className="custom-select my-2" as="select"
                   name={name}
                   onChange={handleChange}>
                <option value=''>{placeholder}</option>
                {values.map(x => <option value={x.name}>{x.name}</option>)}
            </Field>
            <span className="custom-asterisk">*</span>
        </div>
        <ErrorMessage name={name}>
            {msg => <div className="err-msg">{msg}</div>}
        </ErrorMessage>
    </>
)

export default SelectComponent;