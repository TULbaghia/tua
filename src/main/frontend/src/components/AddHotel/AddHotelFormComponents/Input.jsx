import React from "react";

export default function Input({type, field, form, handleChange, fieldName, obligatory, placeholder}) {
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
                required
            />
            {obligatory && <span className="custom-asterisk">*</span>}
        </div>
    )
}