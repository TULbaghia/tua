import React from "react";

export default function Input({type, field, form, handleChange, fieldName, placeholder, obligatory = false}) {
    return (
        <div className="d-flex w-100">
            <input
                {...field}
                onChange={e => {
                    handleChange(e)
                    form.setFieldValue(fieldName, e.target.value)
                }}
                placeholder={placeholder}
                className="form-control my-2"
                type={type}
                required={obligatory}
            />
            {obligatory && <span className="custom-asterisk">*</span>}
        </div>
    )
}