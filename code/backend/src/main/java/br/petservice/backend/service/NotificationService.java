package br.petservice.backend.service;

import br.petservice.backend.model.MessageSend;
import br.petservice.backend.repository.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Integer.parseInt;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationService {

    private final MessageRepository messageRepository;
    private final RabbitAdmin rabbitAdmin;
    private final SimpMessagingTemplate messagingTemplate;
    private final ConnectionFactory connectionFactory;
    private final Map<String, SimpleMessageListenerContainer> listeners = new ConcurrentHashMap<>();

    public void addQueueListener(String routingKey) {
        initializeMessageListener(routingKey);
    }


    public boolean existActiveListener(String routingKey) {
        return listeners.containsKey(routingKey);
    }

    public void creteNewQueueListener(int sender, int recipient) {

        String queueName = "chat_topic_" + sender + "_" + recipient;
        String exchangeName = "chat_exchange";
        String routingKey = "chat_topic_" + sender + "_" + recipient;

        Queue queue = new Queue(queueName, true);
        TopicExchange exchange = new TopicExchange(exchangeName);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);

        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * Inicializa o listener de mensagens para uma comunidade específica.
     *
     * @param queueName O identificador da comunidade para a qual o listener será inicializado.
     */
    private void initializeMessageListener(String queueName) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(createMessageListener(queueName));
        container.start();

        listeners.put(queueName, container);
    }

    private MessageListenerAdapter createMessageListener(String queueName) {

        int sender = parseInt(queueName.split("_")[2]);
        int recipient = parseInt(queueName.split("_")[3]);

        return new MessageListenerAdapter(new Object() {
            public void handleMessage(String message) {

                messageRepository.save(new MessageSend(
                        Integer.min(sender, recipient) + "-" + Integer.max(sender, recipient), message, sender, recipient));

                try {
                    messagingTemplate.convertAndSend("/topic/messages/" + recipient + "/" + sender, message);
                } catch (MessagingException e) {
                    throw new RuntimeException(
                            "Erro ao enviar mensagem para usuário " + queueName + " pelo WebSocket", e);
                }
            }
        });
    }


}