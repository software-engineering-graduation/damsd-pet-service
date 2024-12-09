import React, { useState, useEffect, useRef } from 'react';
import ChatSidebar from '../organisms/ChatSidebar';
import ChatWindow from '../organisms/ChatWindow';
import DashboardLayout from '../templates/DashboardLayout';
import ScheduleModal from '../molecules/ScheduleModal';
import authService from '../../services/authService';
import webSocketService, { sendMessage as sendWebSocketMessage } from '../../services/webSocketService';
import { v4 as uuidv4 } from 'uuid';
import './Chat.scss';

const Chat = ({ user }) => {
    const [chats, setChats] = useState([]);
    const [selectedChat, setSelectedChat] = useState(null);
    const [messages, setMessages] = useState([]);
    const [isLoadingMessages, setIsLoadingMessages] = useState(false);
    const [isLoadingLatest, setIsLoadingLatest] = useState(false);
    const [isScheduleModalOpen, setIsScheduleModalOpen] = useState(false);
    const [scheduleToEdit, setScheduleToEdit] = useState(null);
    const [services, setServices] = useState([]);
    const [pets, setPets] = useState([]);
    const stompClientRef = useRef(null);

    useEffect(() => {
        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.deactivate();
            }
        };
    }, []);

    useEffect(() => {
        setIsLoadingLatest(true);
        const fetchChats = async () => {
          const response = await authService.getAllConversations(user.id);
          setChats(response);
        };
    
        fetchChats();
        setIsLoadingLatest(false);
    }, [user.id]);

    const fetchServicesAndPets = async () => {
        const tutorId = user.type === 'tutor'
            ? user.id
            : selectedChat.partner.id;
        const establishmentId = user.type === 'empresa'
            ? user.id
            : selectedChat.partner.id;

        const servicesResponse = await authService.getAllEstablishmentServices(establishmentId);
        setServices(servicesResponse || []);

        const petsResponse = await authService.getAllPetsByTutor(tutorId);
        setPets(petsResponse || []);
    };

    const defineCombo = (sender_id, receiver_id) => {
        return sender_id < receiver_id
            ? `${sender_id}-${receiver_id}`
            : `${receiver_id}-${sender_id}`;
    }

    const defineNewMessage = (senderId, text) => {
        return {
            id: uuidv4(),
            sender_id: senderId,
            msg: text
        }
    }

    const handleSelectChat = async (chat) => {
        setIsLoadingMessages(true);

        if (stompClientRef.current) {
            stompClientRef.current.deactivate();
        }

        setSelectedChat(chat);

        stompClientRef.current = webSocketService(user.id, chat.partner.id, (newMessage) => {
            const newMsg = defineNewMessage(chat.partner.id, newMessage);

            setMessages((prev) => {
                return { 
                    ...prev,
                    msgs: [...prev.msgs, newMsg]
                };
            });
        });

        const combo = defineCombo(chat.sender_id, chat.receiver_id);

        const messages = {
            partner: chat.partner,
            msgs: await authService.getAllMessages(combo)
        };

        setMessages(messages || []);
        setIsLoadingMessages(false);
    }

    const handleSendMessage = async (text) => {
        const newMessage = defineNewMessage(user.id, text);

        if (stompClientRef.current) {
            sendWebSocketMessage(stompClientRef.current, user.id, selectedChat.partner.id, text);
        }

        setMessages((prev) => {
            return { 
                ...prev,
                msgs: [...prev.msgs, newMessage]
            };
        });
    }

    const handleOpenScheduleModal = async () => {
        await fetchServicesAndPets();
        setScheduleToEdit(null);
        setIsScheduleModalOpen(true);
    };

    const handleEditScheduleModal = async (scheduleMessage) => {
        setScheduleToEdit(scheduleMessage);
        await fetchServicesAndPets();
        setIsScheduleModalOpen(true);
    };

    const handleScheduleSubmit = async (scheduleData) => {
        const newMessage = defineNewMessage(user.id, JSON.stringify(scheduleData));

        if (stompClientRef.current) {
            sendWebSocketMessage(
                stompClientRef.current,
                user.id,
                selectedChat.partner.id,
                JSON.stringify(scheduleData)
            );
        }

        setMessages((prev) => ({
            ...prev,
            msgs: [...prev.msgs, newMessage],
        }));
    };

    return (
        <DashboardLayout>
            <div className="chat-container">
                <ChatSidebar
                    chats={chats}
                    onSelectChat={handleSelectChat}
                    isLoadingLatest={isLoadingLatest}
                />
                {selectedChat ? (
                    <ChatWindow
                        messages={messages}
                        onSendMessage={handleSendMessage}
                        user={user}
                        isLoadingMessages={isLoadingMessages}
                        onOpenScheduleModal={handleOpenScheduleModal}
                        onEditScheduleModal={handleEditScheduleModal}
                    />
                ) : (
                    <div className="chat-placeholder">Selecione uma conversa para começar</div>
                )}

                {isScheduleModalOpen && (
                    <ScheduleModal
                        isOpen={isScheduleModalOpen}
                        onClose={() => setIsScheduleModalOpen(false)}
                        onScheduleSubmit={handleScheduleSubmit}
                        services={services}
                        pets={pets}
                        initialData={scheduleToEdit}
                    />
                )}
            </div>
        </DashboardLayout>
    );
};

export default Chat;
