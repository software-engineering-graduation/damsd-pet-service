package br.petservice.service;

import br.petservice.backend.repository.MessageRepository;
import br.petservice.backend.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private RabbitAdmin rabbitAdmin;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private ConnectionFactory connectionFactory;

    @InjectMocks
    private NotificationService notificationService;

    private Map<String, SimpleMessageListenerContainer> listeners;

    @BeforeEach
    public void setUp() {
        listeners = new ConcurrentHashMap<>();
        notificationService = new NotificationService(messageRepository, rabbitAdmin, messagingTemplate, connectionFactory);
    }

    @Test
    public void testAddQueueListener() {
        String routingKey = "chat_topic_1_2";
        notificationService.addQueueListener(routingKey);
        assertTrue(notificationService.existActiveListener(routingKey));
    }

    @Test
    public void testExistActiveListener() {
        String routingKey = "chat_topic_1_2";
        assertFalse(notificationService.existActiveListener(routingKey));
        notificationService.addQueueListener(routingKey);
        assertTrue(notificationService.existActiveListener(routingKey));
    }

    @Test
    public void testInitializeMessageListener() {
        String queueName = "chat_topic_1_2";
        notificationService.addQueueListener(queueName);
        assertTrue(notificationService.existActiveListener(queueName));
    }
}