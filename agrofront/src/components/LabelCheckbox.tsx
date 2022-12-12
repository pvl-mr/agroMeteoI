import * as React from 'react'
import {
  FormControlLabel,
  FormControlLabelProps,
  Checkbox as MuiCheckbox,
} from '@mui/material'

export default function LabelCheckbox({
  checked,
  label,
  labelPlacement,
  control = <MuiCheckbox />,
  name,
  onChange,
}: Partial<FormControlLabelProps>): JSX.Element {
  return (
    <FormControlLabel
      checked={checked}
      control={control || <MuiCheckbox />}
      label={label || ''}
      labelPlacement={labelPlacement}
      name={name}
      onChange={onChange}
    />
  )
}
