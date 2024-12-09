package br.petservice.backend.controller;

import br.petservice.backend.service.ChatService;
import br.petservice.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final NotificationService notificationService;

    @MessageMapping("/chat/send/{sender}/{recipient}")
    public void sendMessage(String message,
                            @DestinationVariable int sender,
                            @DestinationVariable int recipient) {

        String routingKey = "chat_topic_" + sender + "_" + recipient;

        if (!notificationService.existActiveListener(routingKey)) {
            notificationService.creteNewQueueListener(sender, recipient);
            notificationService.addQueueListener(routingKey);
        }

        chatService.sendMessage(routingKey, message);
    }
}