import React, { useState, useEffect } from 'react';
import { Box, Typography } from '@mui/material';
import DashboardLayout from '../templates/DashboardLayout';
import Button from '../atoms/Button';
import LoadingSpinner from '../atoms/LoadingSpinner';
import ErrorMessage from '../atoms/ErrorMessage';
import ServiceCard from '../organisms/ServiceCard';
import ServiceDetails from '../organisms/ServiceDetails';
import authService from '../../services/authService';

const Services = () => {
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [index, setIndex] = useState(0);
    const itemsPerPage = 5;
    const [selectedService, setSelectedService] = useState(null);

    useEffect(() => {
        const loadServices = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await authService.getAllServices(index, itemsPerPage);
                setServices((prev) => [...prev, ...response.content]);
            } catch (err) {
                setError('Erro ao carregar serviços: ' + err.message);
            } finally {
                setLoading(false);
            }
        };

        loadServices();
    }, [index]);

    const loadMoreServices = () => {
        setIndex((prevIndex) => prevIndex + 1);
    };

    const openServiceDetails = (service) => {
        setSelectedService(service);
    };

    const closeServiceDetails = () => {
        setSelectedService(null);
    };

    const Header = ({ children }) => (
        <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%', margin: '20px' }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    Todos os Serviços
                </Typography>
            </Box>
            {children}
        </Box>
    );

    if (loading && services.length === 0) {
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
                <Box>
                    {services.map((service) => (
                        <ServiceCard
                            key={service.id}
                            service={service}
                            onClick={() => openServiceDetails(service)}
                        />
                    ))}
                </Box>

                {services.length > 0 && services.length % itemsPerPage === 0 && (
                    <Button
                        onClick={loadMoreServices}
                        sx={{ marginTop: '20px' }}
                    >
                        Carregar Mais
                    </Button>
                )}
            </Header>

            {selectedService && (
                <ServiceDetails
                    service={selectedService}
                    onClose={closeServiceDetails}
                />
            )}
        </DashboardLayout>
    );
};

export default Services;
