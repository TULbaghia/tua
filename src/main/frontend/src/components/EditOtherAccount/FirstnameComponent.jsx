import React from "react";
import {ErrorMessage, Field} from "formik";
import FirstnameInput from "./FirstnameInput";

export default function FirstnameComponent({name, placeholder, handleChange}) {
    return (
        <>
            <Field name={name}
                   render={({field, form}) => (
                       <FirstnameInput
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
