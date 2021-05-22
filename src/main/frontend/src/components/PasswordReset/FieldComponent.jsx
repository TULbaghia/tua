import React from "react";
import {ErrorMessage, Field} from "formik";
import PasswordInput from "./PasswordInput";

export default function FieldComponent({name, placeholder, handleChange}) {
    return (
        <>
            <Field type="password"
                   name={name}
                   render={({field, form}) => (
                       <PasswordInput
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
