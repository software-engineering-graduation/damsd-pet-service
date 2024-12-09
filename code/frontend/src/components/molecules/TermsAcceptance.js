import React from 'react';
import PropTypes from 'prop-types';
import { FormControlLabel, Checkbox } from '@mui/material';

const TermsAcceptance = ({ accepted, onChange, label }) => {
    return (
        <div className="terms-acceptance" style={{ marginTop: '20px' }}>
            <FormControlLabel control={
                <Checkbox
                    checked={accepted}
                    onChange={onChange}
                    color="primary">
                </Checkbox>}
                label={label}>
            </FormControlLabel>
        </div>
    );
};

TermsAcceptance.propTypes = {
    accepted: PropTypes.bool,
    onChange: PropTypes.func.isRequired,
    label: PropTypes.string,
};

export default TermsAcceptance;
