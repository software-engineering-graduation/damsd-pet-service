import React from "react";
import PropTypes from "prop-types";
import TextField from "@mui/material/TextField";
import IconButton from "@mui/material/IconButton";
import ClearIcon from "@mui/icons-material/Clear";

const InputField = ({
    label,
    type = 'text',
    value,
    variant = "filled",
    onChange,
    error,
    onClear,
    endAdornment,
    InputLabelProps,
    multiline,
    maxRows,
    select = false,
    fullWidth = false,
    placeholder,
    children,
    sx
}) => {
    return (
        <div className="form-group">
            <TextField
                className="input-field"
                label={label}
                placeholder={placeholder}
                type={type}
                value={value}
                onChange={onChange}
                error={Boolean(error)}
                helperText={error}
                variant={variant}
                multiline={multiline}
                maxRows={maxRows}
                select={select}
                InputProps={{
                    endAdornment: (
                        <>
                            {endAdornment}
                            {onClear && (
                                <IconButton onClick={onClear} size="small">
                                    <ClearIcon />
                                </IconButton>
                            )}
                        </>
                    ),
                }}
                fullWidth={fullWidth}
                sx={{
                    marginBottom: '16px',
                    width: '100%',
                    maxWidth: '400px',
                    ...sx
                }}
                InputLabelProps={InputLabelProps}
            >
                {children}
            </TextField>
        </div>
    );
};

InputField.propTypes = {
    label: PropTypes.string.isRequired,
    type: PropTypes.string,
    value: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.number
    ]).isRequired,
    variant: PropTypes.string,
    onChange: PropTypes.func.isRequired,
    error: PropTypes.string,
    onClear: PropTypes.func,
    endAdornment: PropTypes.node,
    children: PropTypes.node,
};

export default InputField;

