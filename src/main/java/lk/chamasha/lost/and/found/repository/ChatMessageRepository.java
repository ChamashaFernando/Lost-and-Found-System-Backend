package lk.chamasha.lost.and.found.repository;

import lk.chamasha.lost.and.found.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatSession_Id(Long chatSessionId);
}
