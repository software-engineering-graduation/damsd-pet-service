package br.petservice.backend.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;

    public ChatService(RabbitTemplate rabbitTemplate, @Value("${spring.rabbitmq.template.exchange}") String exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendMessage(String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}