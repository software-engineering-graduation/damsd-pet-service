import React, { useState } from 'react';
import IconButton from "@mui/material/IconButton";
import MicIcon from '@mui/icons-material/Mic';
import Button from '../atoms/Button';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import InputField from '../atoms/InputField';

const MessageInput = ({ onSend, onOpenScheduleModal, isTutorToEstablishment }) => {
    const [message, setMessage] = useState('');

    const handleSend = () => {
        if (message.trim()) {
            onSend(message);
            setMessage('');
        }
    };

    return (
        <div className="chat-input">
            <div className="input-buttons">
                {isTutorToEstablishment && (
                    <IconButton onClick={onOpenScheduleModal}>
                        <CalendarTodayIcon />
                    </IconButton>
                )}
            </div>
            <InputField
                type="text"
                placeholder="Escreva uma mensagem..."
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                fullWidth
                sx={{ marginBottom: 0, maxWidth: 'unset' }}
            />
            <IconButton className="mic-button">
                <MicIcon />
            </IconButton>
            <Button onClick={handleSend} disabled={!message.trim()}>Enviar</Button>
        </div>
    );
};

export default MessageInput;
