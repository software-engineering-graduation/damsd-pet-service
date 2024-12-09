import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const webSocketService = (senderId, receiverId, onMessageReceived) => {
    const socket = new SockJS('https://pet-service-api-d0hxdharh6d3bgd2.eastus2-01.azurewebsites.net/ws');
    const stompClient = new Client({
        webSocketFactory: () => socket,
        debug: (str) => console.log(str),
        onConnect: () => {
            console.log(`Conectado ao WebSocket`);
            stompClient.subscribe(`/topic/messages/${senderId}/${receiverId}`, (message) => {
                console.log('mensagem chegando: ', message);
                onMessageReceived(message.body);
            });
        },
        onStompError: (frame) => {
            console.error('Erro no STOMP', frame.headers['message']);
        },
    });

    stompClient.activate();

    return stompClient;
};

export const sendMessage = (stompClient, senderId, receiverId, message) => {
    stompClient.publish({
        destination: `/app/chat/send/${senderId}/${receiverId}`,
        body: message,
    });
};

export default webSocketService;
