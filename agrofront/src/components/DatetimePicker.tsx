import * as React from 'react'
import TextField, { TextFieldProps } from '@mui/material/TextField'

export default function DatetimePicker({
  label,
  id,
  defaultValue,
  onChange,
  value,
  name,
}: TextFieldProps): JSX.Element {
  return (
    <TextField
      id={id}
      label={label}
      value={value}
      type="datetime-local"
      defaultValue={defaultValue}
      onChange={onChange}
      name={name}
      sx={{ width: 250 }}
      InputLabelProps={{
        shrink: true,
      }}
    />
  )
}
