package lk.chamasha.lost.and.found.repository;

import lk.chamasha.lost.and.found.model.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    Optional<ChatSession> findById(Long id);
    List<ChatSession> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);
}