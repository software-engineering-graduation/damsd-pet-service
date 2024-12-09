import React from 'react';
import Button from './Button';
import { Box } from '@mui/material';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';

const MessageBubble = ({ text, type, onAction }) => {
    const requiredFields = [
        { name: 'service', type: 'object' },
        { name: 'pet', type: 'object' },
        { name: 'date', type: 'string' },
        { name: 'time', type: 'string' }
    ];

    const isValidScheduleMessage = (message, requiredFields) => {
        try {
            const json = JSON.parse(message);

            for (const field of requiredFields) {
                if (!json.hasOwnProperty(field.name) || typeof json[field.name] !== field.type) {
                    return false;
                }
            }
            return true;
        } catch (e) {
            return false;
        }
    };

    const message = isValidScheduleMessage(text, requiredFields) ? JSON.parse(text) : null;

    const formatDate = (dateString) => format(new Date(dateString), 'dd/MM/yyyy', { locale: ptBR });
    const formatTime = (timeString) => format(new Date(timeString), 'HH:mm', { locale: ptBR });

    const cancelAction = type === 'other' && message?.status === 'requested'
        ? { action: 'decline', title: 'Recusar' }
        : { action: 'cancel', title: 'Cancelar' }

    return (
        <div className={`message ${type} ${message?.status}`}>
            {message ? (
                <>
                    <p
                        style={{ marginTop: '0px', textAlign: 'center' }}
                    >
                        Agendamento {
                            (() => {
                                switch (message.status) {
                                    case 'canceled':
                                        return 'cancelado';
                                    case 'accepted':
                                        return 'confirmado';
                                    case 'declined':
                                        return 'recusado';
                                    default:
                                        return 'solicitado';
                                }
                            })()
                        }
                    </p>
                    <p>Serviço: {message.service.name}<br />
                    Valor: R$ {message.service.value?.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}<br />
                    Pet: {message.pet.name}<br />
                    Data: {formatDate(message.date)}<br />
                    Horário: {formatTime(message.time)}</p>
                    {message.status !== 'canceled' && message.status !== 'declined' && (
                        <div className="schedule-actions">
                            {type === 'other' && message.status === 'requested' && (
                                <Button sx={{ marginBottom: '10px' }} color='success' onClick={() => onAction("accept", message)}>Aceitar</Button>
                            )}
                            <Box sx={{ display: 'inline-flex', width: '100%', justifyContent: 'space-between' }}>
                                <Box sx={{ width: 'calc(50% - 5px)' }}>
                                    <Button sx={{ marginBottom: '5px' }} color='warning' onClick={() => onAction("edit", message)}>Editar</Button>
                                </Box>
                                <Box sx={{ width: 'calc(50% - 5px)' }}>
                                    <Button sx={{ marginBottom: '5px'}} color='error' onClick={() => onAction(cancelAction.action, message)}>{cancelAction.title}</Button>
                                </Box>
                            </Box>
                        </div>
                    )}
                </>
            ) : (
                <span>{text}</span>
            )}
        </div>
    );
}

export default MessageBubble;
