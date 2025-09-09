package lk.chamasha.lost.and.found.repository;

import lk.chamasha.lost.and.found.model.ClaimRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRequestRepository extends JpaRepository<ClaimRequest, Long> {
    Optional<ClaimRequest> findById(Long id);
    List<ClaimRequest> findByUserId(Long userId);
    List<ClaimRequest> findByItemId(Long itemId);
}
