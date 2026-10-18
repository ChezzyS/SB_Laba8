package ru.arkhipov.lab8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.arkhipov.lab8.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
