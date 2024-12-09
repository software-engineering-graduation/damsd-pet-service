package br.petservice.backend.repository;

import br.petservice.backend.model.MessageSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageSend, Long> {

    List<MessageSend> findAllByComboIdOrderByCreatedAt(String comboId);

    @Query(value = "SELECT * FROM message m WHERE m.created_at = (SELECT MAX(m2.created_at) FROM message m2 WHERE m2.combo_id = m.combo_id)", nativeQuery = true)
    List<MessageSend> findTopByComboIdOrderByCreatedAtDesc();
}
