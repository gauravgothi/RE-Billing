package in.co.mpwin.rebilling.jwt.repository;

import in.co.mpwin.rebilling.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
