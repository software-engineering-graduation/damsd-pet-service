import React from 'react';
import PropTypes from 'prop-types';
import { Box } from '@mui/material';
import logo from '../../assets/images/PetService_Top.png';

const Logo = () => (
    <Box
        component="img"
        src={logo}
        alt="Pet Service Logo"
        sx={{
            width: '100%',
            maxWidth: '200px',
            margin: '20px 0',
            '@media (max-width: 600px)': {
                maxWidth: '150px'
            }
        }}
    />
);

Logo.propTypes = {
    src: PropTypes.string,
};

export default Logo;
