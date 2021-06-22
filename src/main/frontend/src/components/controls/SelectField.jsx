import React from 'react'
import Select from 'react-select'

export default function SelectField({
  options,
  field,
  form,
  styles
}){
    return (
        <Select
        options={options}
        name={field.name}
        value={options ? options.find(option => option.value === field.value) : ''}
        onChange={(option) => form.setFieldValue(field.name, option.value)}
        onBlur={field.onBlur}
        styles={styles}
      />
    )
}
