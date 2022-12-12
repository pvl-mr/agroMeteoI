import React from 'react'
import InputMask, { Props } from 'react-input-mask'
import { TextField, TextFieldProps } from '@mui/material'

const TextFieldMask: any = ({
  ...props
}: Props & TextFieldProps): JSX.Element => {
  const { value, onChange, onBlur, mask } = props
  return (
    <InputMask mask={mask} value={value} onChange={onChange} onBlur={onBlur}>
      {(inputProps: Props & TextFieldProps) => (
        <TextField {...props} {...inputProps} />
      )}
    </InputMask>
  )
}

export default TextFieldMask
