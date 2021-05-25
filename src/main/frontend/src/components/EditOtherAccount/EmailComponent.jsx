import React from "react";
import {ErrorMessage, Field} from "formik";
import EmailInput from "./EmailInput";

export default function EmailComponent({name, placeholder, handleChange}) {
    return (
        <>
            <Field name={name}
                   render={({field, form}) => (
                       <EmailInput
                           field={field}
                           form={form}
                           placeholder={placeholder}
                           handleChange={handleChange}
                           fieldName={name}/>
                   )}/>
            <ErrorMessage name={name}>
                {msg => <div style={{color: 'red'}}>{msg}</div>}
            </ErrorMessage>
        </>
    )
}
