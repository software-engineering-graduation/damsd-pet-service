import React from 'react';
import { Card, CardContent, Typography, Rating, Box, Tooltip } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';

const MyServiceCard = ({ service, onEdit }) => (
    <Card sx={{ marginBottom: '20px', overflow: 'visible' }}>
        <CardContent sx={{ paddingBottom: '16px !important' }}>
            <Typography variant="h6" sx={{ display: "flex", justifyContent: "space-between" }}>
                <Box>
                    {service?.name || '-'}&nbsp;
                    <Tooltip title='Editar'>
                        <EditIcon
                            sx={{ color: '#333', fontSize: 'medium', cursor: 'pointer' }}
                            onClick={() => onEdit(service)}
                        />
                    </Tooltip>
                </Box>
                {/* <Rating name="read-only" value={service.rating} readOnly precision={0.5} /> */}
            </Typography>
            {service?.description && (
                <Typography variant="body2">
                    {service.description}
                </Typography>
            )}
            <Typography variant="body2">
                {service?.category_name
                    ? service.category_name
                    : '(sem categoria)'}
                {' - '}
                {service?.value
                    ? `R$ ${service.value}`
                    : '(valor não definido)'
                }
            </Typography>
        </CardContent>
    </Card>
);

export default MyServiceCard;
