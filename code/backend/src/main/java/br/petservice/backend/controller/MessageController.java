package br.petservice.backend.controller;

import br.petservice.backend.model.MessageSend;
import br.petservice.backend.service.MessageServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageServiceImpl messageService;

    @GetMapping("/{comboId}")
    public ResponseEntity<Map<Long, String>> carrergarHistorico(@PathVariable String comboId) {
        return ResponseEntity.ok(messageService.carregarHistorico(comboId));
    }

    @GetMapping("/last")
    public ResponseEntity<List<MessageSend>> findTopByComboIdOrderByCreatedAtDesc() {
        return ResponseEntity.ok(messageService.findTopByComboIdOrderByCreatedAtDesc());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<Long, String>> atualizar(@PathVariable Long id, @RequestBody MessageSend messageSend) {
        return ResponseEntity.ok(messageService.atualizar(id, messageSend));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        messageService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
