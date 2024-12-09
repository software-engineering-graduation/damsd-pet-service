import React from 'react';
import PropTypes from 'prop-types';
import { Button as MUIButton } from '@mui/material';

const Button = ({ onClick, type = 'button', children, variant = 'contained', color = 'primary', alignment = 'center', fullWidth, radius, marginTop, disabled = false, height, sx }) => {
    return (
        <div style={{ textAlign: alignment, marginTop: marginTop }}>
            <MUIButton
                type={type}
                variant={variant}
                color={color}
                onClick={onClick}
                fullWidth={fullWidth}
                disabled={disabled}
                sx={{
                    ...sx,
                    borderRadius: radius,
                    height: height,
                    width: '100%',
                    maxWidth: fullWidth ? 'none' : '300px'
                }}
            >
                {children}
            </MUIButton>
        </div>
    );
};

Button.propTypes = {
    onClick: PropTypes.func,
    type: PropTypes.string,
    children: PropTypes.node.isRequired,
    variant: PropTypes.string,
    color: PropTypes.string,
    alignment: PropTypes.string,
    fullWidth: PropTypes.bool,
    radius: PropTypes.string,
    marginTop: PropTypes.string,
    disabled: PropTypes.bool,
    height: PropTypes.string,
};

export default Button;
