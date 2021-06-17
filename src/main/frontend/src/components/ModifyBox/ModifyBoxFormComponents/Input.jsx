import React from "react";

export default function Input({type, field, form, handleChange, fieldName, placeholder, readonly}) {
    return (
        <div className="d-flex w-100">
            <input
                {...field}
                onChange={e => {
                    handleChange(e)
                    form.setFieldValue(fieldName, e.target.value)
                }}
                className="form-control my-2"
                placeholder={placeholder}
                type={type}
                required
                readOnly={readonly}
                autoFocus={true}
            />
            {readonly ? <></> : <span className="custom-asterisk">*</span>}
        </div>
    )
}