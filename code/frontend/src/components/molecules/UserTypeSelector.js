import React from 'react';
import PropTypes from 'prop-types';
import ButtonGroup from './ButtonGroup';

const UserTypeSelector = ({ label, selectedType, onChange, options, style }) => {
    return (
        <div className="labeled-type-selector"  style={style}>
            <label style={{ marginRight:'20px' }}>{label}</label>
            <ButtonGroup 
                options={options} 
                selectedType={selectedType} 
                onChange={onChange} 
            />
        </div>
    );
};

UserTypeSelector.propTypes = {
    label: PropTypes.string.isRequired,
    selectedType: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    options: PropTypes.arrayOf(
        PropTypes.shape({
            type: PropTypes.string.isRequired,
            label: PropTypes.string.isRequired,
        })
    ).isRequired,
    style: PropTypes.object,
};

export default UserTypeSelector;
