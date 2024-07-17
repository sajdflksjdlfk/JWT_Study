package codehows.dream.newsproject.repository;

import codehows.dream.newsproject.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByUserName(String username);

    User findByUserName(String username);
}
