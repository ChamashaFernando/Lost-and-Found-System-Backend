package lk.chamasha.lost.and.found.repository;

import lk.chamasha.lost.and.found.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
//    Optional<User> findByIdAndVerified(Long id, boolean verified);
//    List<User> findByVerified(boolean verified);
//  Optional<User> findByUsername(String username);

}
