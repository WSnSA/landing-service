package mn.landing.landing_service.Repository;

import mn.landing.landing_service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByRegisterNumber(String registerNumber);
    boolean existsByPhone(String phone);
}

