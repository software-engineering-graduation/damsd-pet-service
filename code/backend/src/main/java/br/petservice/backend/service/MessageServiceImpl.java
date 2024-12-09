package br.petservice.backend.service;

import br.petservice.backend.model.MessageSend;
import br.petservice.backend.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@AllArgsConstructor
public class MessageServiceImpl {

    private final MessageRepository messageRepository;

    public Map<Long, String> carregarHistorico(String comboId) {
        return messageRepository.findAllByComboIdOrderByCreatedAt(comboId).stream().collect(
                Collectors.toMap(MessageSend::getId, x -> String.format("%s: %s", x.getSenderId(), x.getMessage())));
    }

    public List<MessageSend> findTopByComboIdOrderByCreatedAtDesc() {
        return messageRepository.findTopByComboIdOrderByCreatedAtDesc();
    }

    public Map<Long, String> atualizar(Long id, MessageSend messageSend) {
        Optional<MessageSend> message = messageRepository.findById(id);
        if (message.isPresent()) {
            copyProperties(messageSend, message.get(), "id", "createdAt", "updatedAt");
            messageRepository.save(message.get());
        }
        return null;
    }

    public void deletar(Long id) {
        messageRepository.deleteById(id);
    }
}
