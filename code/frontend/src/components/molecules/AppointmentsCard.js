import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import { format, isBefore } from 'date-fns';

const AppointmentsCard = ({ appointment, user }) => {
    const formattedDate = format(new Date(appointment.date_time), 'dd/MM/yyyy');
    const formattedTime = format(new Date(appointment.date_time), 'HH:mm');
    const isPastAppointment = isBefore(new Date(appointment.date_time), new Date());

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
        <Card sx={{ 
            marginBottom: '20px',
            overflow: 'visible',
            backgroundColor: isPastAppointment ? '#f5f5f5' : '#ffffff',
            border: '1px solid',
            borderColor: isPastAppointment ? '#cccccc' : '#000000'
        }}>
            <CardContent sx={{ paddingBottom: '16px !important' }}>
            <Typography variant="h6" sx={{ display: "flex", alignItems: "center" }}>
                    <AccessTimeIcon sx={{ marginRight: '8px' }} />
                    {formattedTime}
                    <CalendarTodayIcon sx={{ marginX: '8px' }} />
                    {formattedDate}
                </Typography>

                <Typography variant="body2" sx={{ marginTop: '8px' }}>
                    <strong>Pet:</strong> {appointment.pet?.name || '-'}
                </Typography>

                <Typography variant="body2" sx={{ marginTop: '8px' }}>
                    <strong>{user.type === 'tutor' ? 'Estabelecimento' : 'Cliente'}: </strong>
                    {user.type === 'tutor'
                        ? appointment.pet_establishment?.business_name || '(nome do estabelecimento não definido)'
                        : appointment.pet_guardian?.full_name || '(nome do cliente não definido)'
                    }
                    {user.phone_number && (
                        <>
                            <span style={{ margin: '0 8px', fontSize: '1.2rem' }}>•</span>
                            {formatPhoneNumber(user.phone_number)}
                        </>
                    )}
                </Typography>

                {user.type === 'tutor' && (
                    <Typography variant="body2" sx={{ marginTop: '8px' }}>
                        <strong>Endereço: </strong>
                        {appointment.pet_establishment.address.street}
                        {appointment.pet_establishment.address.street_number && (`, ${appointment.pet_establishment.address.street_number}`)}
                        {appointment.pet_establishment.address.additional_information && (`, ${appointment.pet_establishment.address.additional_information}`)}
                        {appointment.pet_establishment.address.neighborhood && (`, ${appointment.pet_establishment.address.neighborhood}`)}
                        {appointment.pet_establishment.address.city && (`, ${appointment.pet_establishment.address.city}`)}
                        {appointment.pet_establishment.address.state && (`/${appointment.pet_establishment.address.state}`)}
                    </Typography>
                )}

                <Typography variant="body2" sx={{ marginTop: '8px' }}>
                    <strong>Serviços Agendados: </strong>
                    {appointment.total_value ? `R$ ${appointment.total_value}` : '(valor não definido)'}
                    <ul style={{ paddingLeft: '16px' }}>
                        {appointment.appointment_services.length > 0 ? (
                            appointment.appointment_services.map((service, index) => (
                                <li key={index}>{service.name}</li>
                            ))
                        ) : (
                            <li>(nenhum serviço agendado)</li>
                        )}
                    </ul>
                </Typography>
            </CardContent>
        </Card>
    );
};

export default AppointmentsCard;
