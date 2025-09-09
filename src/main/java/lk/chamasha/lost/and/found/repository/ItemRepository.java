package lk.chamasha.lost.and.found.repository;

import lk.chamasha.lost.and.found.model.Item;
import lk.chamasha.lost.and.found.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findById(Long id);
    List<Item> findByStatus(ItemStatus status);
    List<Item> findByUserId(Long userId);
    List<Item> findByLocationContainingIgnoreCase(String location);
    List<Item> findByCategory(String category);
    List<Item> findByEmergencyTrue();
}
