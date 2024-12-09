import React from 'react';
import { Box, Typography } from '@mui/material';

const EstablishmentCard = ({ establishment }) => (
    <Box sx={{ padding: '16px', marginBottom: '10px', border: '1px solid #e0e0e0', borderRadius: '8px' }}>
        <Typography variant="h6">{establishment.name} • {establishment.company}</Typography>
        <Typography variant="body1">{establishment.type} • R$ {establishment.price}</Typography>
        <Typography variant="body2">{establishment.description}</Typography>
        <Typography variant="body2">Endereço: {establishment.address}</Typography>
        <Typography variant="body2">Contato: {establishment.contact}</Typography>
        <Typography variant="body2">Avaliação: {establishment.rating}/5</Typography>
    </Box>
);

export default EstablishmentCard;
