import React from 'react'
import Select from 'react-select'
import { useField, useFormikContext } from "formik";

export default function SelectField({ ...props }) {
  const { setFieldValue } = useFormikContext();
  const [field, fieldMeta, fieldHelpers] = useField(props);
  const { options} = props
  let value = options ? options.find(option => option.value === field.value) : ''
  value = value === undefined ? null : value
  console.log("value");
  console.log(value);
  return (
    <Select
      {...field}
      {...props}
      value={value}
      onChange={option => {
        setFieldValue(field.name, option.value);
      }}
      onBlur={field.onBlur}
      name={field.name}
    />
  );
};
