import {ErrorMessage, Field, Form} from "formik";
import React from "react";
import Input from "./Input";

export default function FieldComponent({type, label, name, handleChange}) {
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
                       handleChange={handleChange}
                       fieldName={name}/>
               )}/>
            <ErrorMessage name={name}>
                {msg => <div className="err-msg">{msg}</div>}
            </ErrorMessage>
        </>
    )
}