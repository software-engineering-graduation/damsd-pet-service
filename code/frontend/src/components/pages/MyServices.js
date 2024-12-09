import React, { useState, useEffect } from 'react';
import { Typography, Box } from '@mui/material';
import { Link } from 'react-router-dom';
import authService from '../../services/authService';
import DashboardLayout from '../templates/DashboardLayout';
import LoadingSpinner from '../atoms/LoadingSpinner';
import ErrorMessage from '../atoms/ErrorMessage';
import ServiceForm from '../organisms/ServiceForm';
import MyServiceCard from '../molecules/MyServiceCard';
import Button from '../atoms/Button';

const MyServices = ({user}) => {
    const [services, setServices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [index, setIndex] = useState(0);
    const itemsPerPage = 4;

    const [showForm, setShowForm] = useState(false);
    const [serviceToEdit, setServiceToEdit] = useState(null);

    useEffect(() => {
        const fetchServices = async () => {
            setLoading(true);
            setError(null);

            try {
                const response = await authService.getEstablishmentServices(user.id, index, itemsPerPage);
                setServices((prev) => [...prev, ...response.content]);
            } catch (err) {
                setError('Erro ao carregar serviços');
            } finally {
                setLoading(false);
            }
        };

        fetchServices();
    }, [index, user.id]);

    const loadMoreServices = () => {
        setIndex((prevIndex) => prevIndex + 1);
    };

    const handleNewService = () => {
        setServiceToEdit(null);
        setShowForm(true);
    };

    const handleEditService = (service) => {
        setServiceToEdit(service);
        setShowForm(true);
    };

    const Header = ({ children }) => (
        <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%', margin: '20px' }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    Meus Serviços
                </Typography>
                {!showForm && (
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleNewService}
                    >
                        Novo Serviço
                    </Button>
                )}
            </Box>
            {!showForm && (
                <Typography variant="body1" sx={{ marginBottom: '20px' }}>
                    Encontre nesta página os serviços prestados pelo seu estabelecimento.
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
                {showForm ? (
                    <ServiceForm setShowForm={setShowForm} serviceToEdit={serviceToEdit} />
                ) : (
                    services.length === 0 ? (
                        <Typography variant="body1">
                            Ainda não existem serviços oferecidos. Cadastre seus serviços&nbsp;
                            <Link onClick={() => setShowForm(true)} sx={{ marginLeft: '5px', cursor: 'pointer' }}>
                                aqui
                            </Link>.
                        </Typography>
                    ) : (
                        services.map((service) => <MyServiceCard key={service.id} service={service} onEdit={handleEditService} />)
                    )
                )}

                {!showForm && services.length > 0 && services.length % itemsPerPage === 0 && (
                    <Button onClick={loadMoreServices}>
                        Carregar mais
                    </Button>
                )}
            </Header>
        </DashboardLayout>
    );
};

export default MyServices;
