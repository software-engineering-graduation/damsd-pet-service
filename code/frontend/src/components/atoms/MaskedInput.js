import TextField from "@mui/material/TextField";
import React from "react";
import IconButton from "@mui/material/IconButton";
import ClearIcon from "@mui/icons-material/Clear";
import InputMask from "react-input-mask";

const MaskedInput = ({
                       mask,
                       value,
                       onChange,
                       label,
                       type = "text",
                       variant = "filled",
                       error,
                       onClear,
                       endAdornment,
                       InputLabelProps,
                     }) => {
  return (
      <div className="form-group">
        <InputMask
            mask={mask}
            value={value}
            onChange={(e) => onChange(e.target.value)}
            maskChar={null}
            alwaysShowMask={false}
        >
          {() => (
              <TextField
                  className="input-field"
                  label={label}
                  type={type}
                  error={Boolean(error)}
                  helperText={error}
                  variant={variant}
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
                  fullWidth
                  sx={{
                    marginBottom: "16px",
                    width: "100%",
                    maxWidth: "400px",
                  }}
                  InputLabelProps={InputLabelProps}
              />
          )}
        </InputMask>
      </div>
  );
};

export default MaskedInput;
