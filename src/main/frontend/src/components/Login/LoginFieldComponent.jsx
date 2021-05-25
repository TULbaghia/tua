import React from "react";
import {ErrorMessage, Field} from "formik";
import LoginInput from "./LoginInput";

export default function LoginFieldComponent({name, placeholder, handleChange}) {
    return (
        <>
            <Field type="text"
                   name={name}
                   render={({field, form}) => (
                       <LoginInput
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
