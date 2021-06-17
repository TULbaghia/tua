import {ErrorMessage, Field} from "formik";
import React from "react";
import Input from "./Input";

export default function FieldComponent({type, label, name, handleChange, placeholder, readonly}) {
    return (
        <>
            <h6 className="mt-3 mb-0">{label}</h6>
            <Field type="text"
                   name={name}
                   render={({field, form}) => (
                       <Input
                           type={type}
                           field={field}
                           form={form}
                           placeholder={placeholder}
                           handleChange={handleChange}
                           fieldName={name}
                           readonly={readonly}/>
                   )}/>
            <ErrorMessage name={name}>
                {msg => <div className="err-msg">{msg}</div>}
            </ErrorMessage>
        </>
    )
}