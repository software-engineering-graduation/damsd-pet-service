package br.petservice.service;

import br.petservice.backend.model.MessageSend;
import br.petservice.backend.repository.MessageRepository;
import br.petservice.backend.service.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;
    private MessageSend messageSend;

        @BeforeEach
    public void setUp() {
        messageSend = new MessageSend();
        messageSend.setId(1L);
        messageSend.setComboId("1-2");
        messageSend.setSenderId(1);
        messageSend.setMessage("Hello");
    }

    @Test
    public void testFindTopByComboIdOrderByCreatedAtDesc() {
        List<MessageSend> messageSends = List.of(
                new MessageSend("1-2", "Ola", 2, 1)
        );

        when(messageRepository.findTopByComboIdOrderByCreatedAtDesc()).thenReturn(messageSends);

        List<MessageSend> result = messageService.findTopByComboIdOrderByCreatedAtDesc();

        assertEquals(1, result.size());
        assertEquals("Ola", result.get(0).getMessage());
    }

    @Test
    public void testCarregarHistorico() {
        when(messageRepository.findAllByComboIdOrderByCreatedAt("1-2")).thenReturn(List.of(messageSend));

        Map<Long, String> result = messageService.carregarHistorico("1-2");

        assertEquals(1, result.size());
        assertEquals("1: Hello", result.get(1L));
    }

    @Test
    public void testAtualizar() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(messageSend));

        MessageSend updatedMessageSend = new MessageSend();
        updatedMessageSend.setMessage("Updated Message");

        messageService.atualizar(1L, updatedMessageSend);

        verify(messageRepository).save(any(MessageSend.class));
        assertEquals("Updated Message", messageSend.getMessage());
    }

    @Test
    public void testDeletar() {
        doNothing().when(messageRepository).deleteById(1L);

        messageService.deletar(1L);

        verify(messageRepository).deleteById(1L);
    }
}