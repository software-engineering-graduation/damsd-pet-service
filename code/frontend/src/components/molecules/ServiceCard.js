import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';

const ServiceCard = ({ service }) => {
    const formatPhoneNumber = (phoneNumber) => {
        if (!phoneNumber) return '(número não disponível)';
        const cleaned = ('' + phoneNumber).replace(/\D/g, '');
        const match = cleaned.match(/^(\d{2})(\d{4,5})(\d{4})$/);
        if (match) {
            return `(${match[1]}) ${match[2]}-${match[3]}`;
        }
        return phoneNumber;
    };
    
    return (
        <Card sx={{ marginBottom: '20px', overflow: 'visible' }}>
            <CardContent sx={{ paddingBottom: '16px !important' }}>
                <Typography variant="h6">
                    {service.name}
                    <span style={{ margin: '0 8px', fontSize: '1.2rem' }}>•</span>
                    {service.pet_establishment.business_name}
                </Typography>
                <Typography variant="body2">
                    {service.category_name || 'Não definido'}
                    <span style={{ margin: '0 8px', fontSize: '1.2rem' }}>•</span>
                    {service.value ? `R$ ${service.value}` : 'A definir'}
                </Typography>
                <Typography variant="body2" color="textSecondary">
                    {service.description}
                </Typography>
                <Typography variant="body2" sx={{ marginTop: '8px' }}>
                    <strong>Endereço: </strong>
                    {service.pet_establishment.address.street}
                    {service.pet_establishment.address.street_number && (`, ${service.pet_establishment.address.street_number}`)}
                    {service.pet_establishment.address.additional_information && (`, ${service.pet_establishment.address.additional_information}`)}
                    {service.pet_establishment.address.neighborhood && (`, ${service.pet_establishment.address.neighborhood}`)}
                    {service.pet_establishment.address.city && (`, ${service.pet_establishment.address.city}`)}
                    {service.pet_establishment.address.state && (`/${service.pet_establishment.address.state}`)}
                </Typography>
                <Typography variant="body2">
                    <strong>Contato: </strong>{formatPhoneNumber(service.pet_establishment.phone_number)}
                </Typography>
            </CardContent>
        </Card>
    );
};

export default ServiceCard;
