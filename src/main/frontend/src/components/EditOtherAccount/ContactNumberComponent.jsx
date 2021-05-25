import React from "react";
import {ErrorMessage, Field} from "formik";
import ContactNumberInput from "./ContactNumberInput";

export default function ContactNumberComponent({name, placeholder, handleChange}) {
    return (
        <>
            <Field name={name}
                   render={({field, form}) => (
                       <ContactNumberInput
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
