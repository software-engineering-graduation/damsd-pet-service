package br.petservice.controller;

import br.petservice.backend.controller.ChatController;
import br.petservice.backend.service.ChatService;
import br.petservice.backend.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ChatController chatController;

    @Test
    void testSendMessage() {
        String message = "Hello";
        int sender = 1;
        int recipient = 2;
        String routingKey = "chat_topic_" + sender + "_" + recipient;

        when(notificationService.existActiveListener(routingKey)).thenReturn(false);
        doNothing().when(notificationService).creteNewQueueListener(sender, recipient);
        doNothing().when(notificationService).addQueueListener(routingKey);
        doNothing().when(chatService).sendMessage(routingKey, message);

        chatController.sendMessage(message, sender, recipient);

        verify(notificationService, times(1)).creteNewQueueListener(sender, recipient);
        verify(notificationService, times(1)).addQueueListener(routingKey);
        verify(chatService, times(1)).sendMessage(routingKey, message);
    }
}