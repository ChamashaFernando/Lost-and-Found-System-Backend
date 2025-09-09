package lk.chamasha.lost.and.found.repository;

import lk.chamasha.lost.and.found.model.OneClickFoundReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OneClickFoundReportRepository extends JpaRepository<OneClickFoundReport, Long> {
//    Optional<OneClickFoundReport> findById(Long id);
//    List<OneClickFoundReport> findByUserId(Long userId);
//    List<OneClickFoundReport> findByCategory(String category);

    List<OneClickFoundReport> findByUserId(Long userId);
}
