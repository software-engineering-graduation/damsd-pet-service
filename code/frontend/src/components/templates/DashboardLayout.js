import React from 'react';
import PropTypes from 'prop-types';
import { DashboardLayout as ToolpadDashboardLayout } from '@toolpad/core';

const DashboardLayout = ({ children }) => {
    return (
        <ToolpadDashboardLayout defaultSidebarCollapsed>
            {children}
        </ToolpadDashboardLayout>
    );
};

DashboardLayout.propTypes = {
    children: PropTypes.node.isRequired,
};

export default DashboardLayout;
