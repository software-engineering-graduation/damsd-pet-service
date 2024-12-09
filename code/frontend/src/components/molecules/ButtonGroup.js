import React from 'react';
import PropTypes from 'prop-types';
import { ButtonGroup as MUIButtonGroup } from '@mui/material';
import Button from '../atoms/Button';

const ButtonGroup = ({ options, selectedType, onChange }) => {
    return (
        <MUIButtonGroup
            variant="outlined"
            aria-label="outlined button group"
            sx={{
                display: 'flex',
                flexWrap: 'wrap',
                justifyContent: 'center'
            }}
        >
            {options.map((option, index) => (
                <Button
                    key={option.type}
                    onClick={() => onChange(option.type)}
                    variant={selectedType === option.type ? 'contained' : 'outlined'}
                    color="primary"
                    radius={options.length === 1 ? '50px' :
                            index === 0 ? '50px 0 0 50px' : 
                            index === options.length - 1 ? '0 50px 50px 0' : 
                            '0'}
                    height="100%"
                >
                    {selectedType === option.type && (
                        <span style={{ marginRight: '5px' }}>&#10003;</span>
                    )}
                    {option.label}
                </Button>
            ))}
        </MUIButtonGroup>
    );
};

ButtonGroup.propTypes = {
    options: PropTypes.arrayOf(
        PropTypes.shape({
            type: PropTypes.string.isRequired,
            label: PropTypes.string.isRequired
        })
    ).isRequired,
    selectedType: PropTypes.string,
    onChange: PropTypes.func.isRequired,
};

export default ButtonGroup;
