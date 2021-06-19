import React from "react";
import { useField, useFormikContext } from "formik";
import DatePicker from "react-datepicker";

export default function DatePickerField({ ...props }) {
  const { setFieldValue } = useFormikContext();
  const [field] = useField(props);
  return (
    <DatePicker
      {...field}
      {...props}
      dateFormat="dd/MM/yyyy"
      selected={(field.value && new Date(field.value)) || null}
      onChange={val => {
        setFieldValue(field.name, val);
      }}
    />
  );
};