import React from "react";
import { useField, useFormikContext } from "formik";
import DatePicker from "react-datepicker";

export default function DatePickerField({ ...props }) {
  const { setFieldValue } = useFormikContext();
  const [field, fieldMeta, fieldHelpers] = useField(props);
  const { changeCallback} = props
  return (
    <DatePicker
      {...field}
      {...props}
      dateFormat="dd/MM/yyyy"
      selected={(field.value && new Date(field.value)) || null}
      onChange={val => {
        fieldHelpers.setTouched(true)
        console.log(changeCallback)
        if(changeCallback !== undefined){
          changeCallback()
        }
        setFieldValue(field.name, val);
      }}
    />
  );
};