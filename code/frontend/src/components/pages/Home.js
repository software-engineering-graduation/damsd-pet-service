import React from 'react';
import DashboardLayout from '../templates/DashboardLayout';
import { Typography } from '@mui/material';

const Home = ({ user }) => {
    return (<DashboardLayout>
        <Typography variant="h4" component="h1" sx={{ margin: '20px' }}>
            Olá, {user ? user.short_name : 'Visitante'}!<br />
            Bem-vindo à PetService
        </Typography>
    </DashboardLayout>);
};

export default Home;
