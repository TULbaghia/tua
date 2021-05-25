import React from "react";
import {ErrorMessage, Field} from "formik";
import PasswordInput from "./PasswordInput";
import LastnameInput from "./LastnameInput";

export default function LastnameComponent({name, placeholder, handleChange}) {
    return (
        <>
            <Field name={name}
                   render={({field, form}) => (
                       <LastnameInput
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
