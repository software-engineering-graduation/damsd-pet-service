import React from "react";
import PropTypes from 'prop-types';
import { Radio as MUIRadio, RadioGroup as MUIRadioGroup } from "@mui/material";
import { FormControl, FormLabel, FormControlLabel } from "@mui/material";

const RadioGroup = ({ value, label, options, onChange, direction = 'row'}) => {
    return (
        <div className="form-group">
            <FormControl>
                <FormLabel id={value}>{label}</FormLabel>
                <MUIRadioGroup
                    aria-labelledby={value}
                    value={value}
                    name="radio-buttons-group"
                    onChange={onChange}
                    sx={{
                        display: 'flex',
                        flexDirection: direction,
                        gap: direction === 'row' ? '16px' : ''
                    }}
                >
                    {options.map((option) => (
                        <FormControlLabel
                            control={<MUIRadio />}
                            key={option.value}
                            value={option.value}
                            label={option.label}
                        />
                    ))}
                </MUIRadioGroup>
            </FormControl>
        </div>
    );
};

RadioGroup.propTypes = {
    value: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    options: PropTypes.arrayOf(
        PropTypes.shape({
            value: PropTypes.string.isRequired,
            label: PropTypes.string.isRequired
        })
    ).isRequired,
    onChange: PropTypes.func.isRequired,
    direction: PropTypes.oneOf(['row', 'column']),
};

export default RadioGroup;
