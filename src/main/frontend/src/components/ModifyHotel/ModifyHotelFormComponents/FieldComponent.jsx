import {ErrorMessage, Field} from "formik";
import React from "react";
import Input from "./Input";

export default function FieldComponent({type, name, placeholder, handleChange}) {
    return (
        <>
            <Field type="text"
               name={name}
               render={({field, form}) => (
                   <Input
                       type={type}
                       field={field}
                       form={form}
                       placeholder={placeholder}
                       handleChange={handleChange}
                       fieldName={name}/>
               )}/>
            <ErrorMessage name={name}>
                {msg => <div className="err-msg">{msg}</div>}
            </ErrorMessage>
        </>
    )
}