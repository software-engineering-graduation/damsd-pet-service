import React, { useState, useEffect } from 'react';
import { Typography, Box } from '@mui/material';
import { Link } from 'react-router-dom';
import authService from '../../services/authService';
import DashboardLayout from '../templates/DashboardLayout';
import LoadingSpinner from '../atoms/LoadingSpinner';
import ErrorMessage from '../atoms/ErrorMessage';
import AppointmentsCard from '../molecules/AppointmentsCard';
import Button from '../atoms/Button';

const Appointments = ({user}) => {
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [index, setIndex] = useState(0);
    const itemsPerPage = 4;

    const showForm = false;

    useEffect(() => {
        const fetchServices = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await authService.getAppointmentServicesByUser(user.type, user.id);
                setServices((prev) => [...prev, ...response]);
            } catch (err) {
                console.log(err);
                setError('Erro ao carregar serviços: ' + err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchServices();
    }, [index, user.type, user.id]);

    const loadMoreServices = () => {
        setIndex((prevIndex) => prevIndex + 1);
    };

    const Header = ({ children }) => (
        <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%', margin: '20px' }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Serviços Agendados
            </Typography>
            {!showForm && user.type === "tutor" && (
                <Typography variant="body1" sx={{ marginBottom: '20px' }}>
                    Veja os seus serviços agendados.
                </Typography>
            )}
            {!showForm && user.type === "empresa" && (
                <Typography variant="body1" sx={{ marginBottom: '20px' }}>
                    Veja os serviços contratados para o seu estabelecimento.
                </Typography>
            )}
            {children}
        </Box>
    );

    if (loading) {
        return (
            <DashboardLayout>
                <Header>
                    <LoadingSpinner />
                </Header>
            </DashboardLayout>
        );
    }

    if (error) {
        return (
            <DashboardLayout>
                <Header>
                    <ErrorMessage message={error} />
                </Header>
            </DashboardLayout>
        );
    }

    return (
        <DashboardLayout>
            <Header>
                {services.length === 0 ? (
                        <Typography variant="body1">
                            Ainda não existem serviços agendados.
                            {user.type === 'tutor'
                                ? ' Encontre serviços para seus pets '
                                : ' Gerencie seus serviços '
                            }
                            clicando&nbsp;
                            <Link to={user.type === 'tutor' ? '/servicos' : '/meus-servicos'}
                                sx={{ marginLeft: '5px', cursor: 'pointer' }}
                            >
                                aqui
                            </Link>.
                        </Typography>
                    ) : (
                        services.map((appointment) => <AppointmentsCard key={appointment.id} appointment={appointment} user={user} />)
                    )
                }

                {!showForm && services.length > 0 && services.length % itemsPerPage === 0 && (
                    <Button variant="contained" onClick={loadMoreServices}>
                        Carregar mais
                    </Button>
                )}
            </Header>
        </DashboardLayout>
    );
};

export default Appointments;
