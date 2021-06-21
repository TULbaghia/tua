import React from "react";
import { useField, useFormikContext } from "formik";
import DataTable from "react-data-table-component"

export default function DataTableField({ ...props }) {
  const { setFieldValue } = useFormikContext();
  const [field] = useField(props);
  return (
    <DataTable
    {...field}
    {...props}
    onSelectedRowsChange={(state) => {
        console.log("elo")
      const ids = state.selectedRows.map(x => x.id)
      setFieldValue(field.name, ids)
    }}
    />
  );
};