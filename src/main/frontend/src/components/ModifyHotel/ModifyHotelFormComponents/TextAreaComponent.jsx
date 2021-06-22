import {ErrorMessage, Field} from "formik";
import React from "react";

function TextAreaComponent({label, name, handleChange, placeholder, obligatory = false}) {
    return (
        <>
            <h6 className="mt-3 mb-0">{label}</h6>
            <div className="d-flex w-100">
                <Field className="text-area-custom w-100 my-2 form-control"
                       as="textarea"
                       name={name}
                       placeholder={placeholder}
                       onChange={handleChange}>
                </Field>
                {obligatory && <span className="custom-asterisk">*</span>}
            </div>
            <ErrorMessage name={name}>
                {msg => <div className="err-msg">{msg}</div>}
            </ErrorMessage>
        </>
    )
}

export default TextAreaComponent;