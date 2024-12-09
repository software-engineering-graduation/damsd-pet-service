package br.petservice.service;

import br.petservice.backend.service.ChatService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @InjectMocks
    private ChatService chatService;

    @Test
    public void testSendMessage() {
        String routingKey = "test.routing.key";
        String message = "Hello, World!";

        chatService.sendMessage(routingKey, message);

        verify(rabbitTemplate).convertAndSend(exchange, routingKey, message);
    }
}