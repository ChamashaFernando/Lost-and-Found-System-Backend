package lk.chamasha.lost.and.found.repository;

import lk.chamasha.lost.and.found.model.Reputation;
import lk.chamasha.lost.and.found.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReputationRepository extends JpaRepository<Reputation, Long> {
//    Optional<Reputation> findById(Long id);
//    List<Reputation> findByUserId(Long userId);
Optional<Reputation> findTopByUserOrderByUpdatedAtDesc(User user);

}
