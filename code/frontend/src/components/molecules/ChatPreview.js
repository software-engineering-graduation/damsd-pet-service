import React from 'react';
import Avatar from '../atoms/Avatar';
import StoreIcon from '@mui/icons-material/Store';
import PersonIcon from '@mui/icons-material/Person';

const ChatPreview = ({ chat, onClick }) => {
    return (
        <div className="chat-preview" onClick={() => onClick(chat)}>
            <Avatar icon={chat.partner.type === 'tutor' ? <PersonIcon /> : <StoreIcon />} />
            <div className="chat-info">
                <p>{chat.partner.name || 'Sem nome ainda'}</p>
                <span>{chat.message || 'Sem mensagens recentes'}</span>
            </div>
        </div>
    );
};

export default ChatPreview;
