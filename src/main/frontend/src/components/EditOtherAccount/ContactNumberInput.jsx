import React from "react";

export default function ContactNumberInput({field, form, handleChange, placeholder, fieldName}) {
    return (
            <div>
                <input
                    {...field}
                    onChange={e => {
                        handleChange(e)
                        form.setFieldValue(fieldName, e.target.value)
                    }}
                    className="form-control my-2"
                    placeholder={placeholder}
                    required
                    autoFocus={true}
                    style={{width: "90%", display: "inline-block"}}
                />
                <div style={{color: "#7749F8", display: "inline-block", margin: "0.2rem"}}>*</div>
            </div>
    )
}
