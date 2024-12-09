import React from 'react';
import ChatPreview from '../molecules/ChatPreview';
import LoadingSpinner from '../atoms/LoadingSpinner';
import { Box } from '@mui/material';

const ChatSidebar = ({ chats, onSelectChat, isLoadingLatest }) => {
    return (
        <div className="chat-sidebar">
            <h2 style={{ padding: '0 10px' }}>Mensagens</h2>
            <div className="chats">
                {isLoadingLatest 
                    ? (
                        <Box sx={{ display: 'flex', height: '100%' }}>
                            <LoadingSpinner />
                        </Box>
                    ) : (
                        chats.map((chat) => (
                            <ChatPreview key={chat.id} chat={chat} onClick={onSelectChat} />
                        )
                    ))
                }
            </div>
        </div>
    );
};

export default ChatSidebar;
