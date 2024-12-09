import React from "react";
import PropTypes from "prop-types";
import TextField from "@mui/material/TextField";
import { MenuItem } from "@mui/material";

const SelectField = ({
  label,
  value,
  variant = "filled",
  onChange,
  error,
  options,
}) => {
  return (
    <div className="form-group">
      <TextField
        className="input-field"
        label={label}
        value={value}
        onChange={onChange}
        error={Boolean(error)}
        variant={variant}
        fullWidth
        select
        sx={{
          marginBottom: "16px",
          width: "100%",
          maxWidth: "400px",
        }}
      >
        {options.map((option) => (
          <MenuItem key={option.key} value={option.value}>{option.label}</MenuItem>
        ))}
      </TextField>
    </div>
  );
};

SelectField.propTypes = {
  label: PropTypes.string.isRequired,
  type: PropTypes.string,
  variant: PropTypes.string,
  onChange: PropTypes.func.isRequired,
  error: PropTypes.string,
  onClear: PropTypes.func,
};

export default SelectField;
