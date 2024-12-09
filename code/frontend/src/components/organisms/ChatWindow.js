import React, { useEffect, useRef, useState } from 'react';
import MessageBubble from '../atoms/MessageBubble';
import MessageInput from '../molecules/MessageInput';
import LoadingSpinner from '../atoms/LoadingSpinner';
import authService from '../../services/authService';

const ChatWindow = ({ messages, onSendMessage, user, isLoadingMessages, onOpenScheduleModal, onEditScheduleModal }) => {
    const messagesEndRef = useRef(null);
    const [isTutorToEstablishment, setIsTutorToEstablishment] = useState(false);

    useEffect(() => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollTop = messagesEndRef.current.scrollHeight;
        }
    }, [messages]);

    useEffect(() => {
        setIsTutorToEstablishment(user.type !== messages?.partner?.type);
    }, [user.type, messages.partner]);

    const formatDate = (dateString, timeString) => {
        const date = dateString.split('T')[0].split('-').reverse().join('/');
        const time = timeString.split('T')[1].split('.')[0];
        return `${date} ${time}`;
    };

    const formatActionResponseMessage = (status, serviceName, petName, date, time) => {
        return `Agendamento ${status}: ${serviceName} para ${petName} em ${date} às ${time}.`;
    }

    const formatMessageToUpdate = (combo, text, senderId, receiverId) => {
        return {
            combo_id: combo,
            message: text,
            sender_id: senderId,
            receiver_id: receiverId
        }
    }
    const handleAction = async (action, data, message) => {
        try {
            switch (action) {
                case 'accept':
                    const appointmentData = {
                        pet_guardian_id: user.type === 'tutor' ? user.id : messages?.partner?.id,
                        pet_id: data.pet.id,
                        pet_establishment_id: user.type === 'empresa' ? user.id : messages?.partner?.id,
                        services_provided: [data.service.id],
                        total_value: data.service.value,
                        date_time: formatDate(data.date, data.time),
                        message: message
                    }

                    const acceptResponse = await authService.acceptAppointment(appointmentData);

                    const aMsg = JSON.parse(message.msg);
                    aMsg.status = 'accepted';
                    aMsg.appointment_id = acceptResponse.id;
                    message.msg = JSON.stringify(aMsg);

                    const acceptedMessage = formatMessageToUpdate(message.combo, message.msg, message.sender_id, message.receiver_id);

                    const resp = await authService.updateMessage(message.id, acceptedMessage);

                    const confirmationMessage = formatActionResponseMessage(
                        'confirmado',
                        data.service.name,
                        data.pet.name,
                        appointmentData.date_time.split(' ')[0],
                        appointmentData.date_time.split(' ')[1].substring(0, 5)
                    );

                    onSendMessage(confirmationMessage);
                    break;

                case 'edit':
                    console.log('Editando agendamento:', data);
                    onEditScheduleModal(data);
                    break;

                case 'decline':
                    const dMsg = JSON.parse(message.msg);
                    dMsg.status = 'declined';
                    message.msg = JSON.stringify(dMsg);

                    const declinedMessage = formatMessageToUpdate(message.combo, message.msg, message.sender_id, message.receiver_id);

                    const declineResponse = await authService.updateMessage(message.id, declinedMessage);
                    const d_date_time = formatDate(data.date, data.time).split(' ');
                    const refusalMessage = formatActionResponseMessage(
                        'recusado',
                        data.service.name,
                        data.pet.name,
                        d_date_time[0],
                        d_date_time[1].substring(0, 5)
                    );
                    onSendMessage(refusalMessage);
                    break;

                case 'cancel':
                    const cMsg = JSON.parse(message.msg);
                    cMsg.status = 'canceled';
                    message.msg = JSON.stringify(cMsg);

                    const canceledMessage = formatMessageToUpdate(message.combo, message.msg, message.sender_id, message.receiver_id);

                    const cancelResponse = await authService.updateMessage(message.id, canceledMessage);
                    const c_date_time = formatDate(data.date, data.time).split(' ');
                    const cancelationMessage = formatActionResponseMessage(
                        'cancelado',
                        data.service.name,
                        data.pet.name,
                        c_date_time[0],
                        c_date_time[1].substring(0, 5)
                    );
                    onSendMessage(cancelationMessage);
                    break;

                default:
                    console.warn('Ação desconhecida:', action);
            }
        } catch (error) {
            console.error('Erro ao processar ação:', action, error);
        }
    };

    return (
        <div className="chat-window">
            {isLoadingMessages ? (
                <div className="spinner-container">
                    <LoadingSpinner />
                </div>
            ) : (
                <>
                    <div className="chat-header">
                        <h2>{messages?.partner?.name || '-'}</h2>
                    </div>
                    <div className="chat-messages" ref={messagesEndRef}>
                        {messages?.msgs?.map((message) => (
                            <MessageBubble
                                key={message.id}
                                text={message.msg}
                                type={message.sender_id == user.id
                                    ? 'user'
                                    : 'other'
                                }
                                onAction={(action, data) => handleAction(action, data, message)}
                            />
                        ))}
                    </div>
                    <MessageInput onSend={onSendMessage} onOpenScheduleModal={onOpenScheduleModal} isTutorToEstablishment={isTutorToEstablishment}/>
                </>
            )}
        </div>
    );
};

export default ChatWindow;
