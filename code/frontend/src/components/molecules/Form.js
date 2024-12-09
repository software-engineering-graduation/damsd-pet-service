import React from 'react';
import PropTypes from 'prop-types';

const Form = ({ children, onSubmit, style }) => {
    return (
        <form onSubmit={e => onSubmit(e)} style={style}>
            {children}
        </form>
    );
};

Form.propTypes = {
    children: PropTypes.node.isRequired,
    onSubmit: PropTypes.func.isRequired,
    style: PropTypes.object,
};

export default Form;
