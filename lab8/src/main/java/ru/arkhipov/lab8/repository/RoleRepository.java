package ru.arkhipov.lab8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.arkhipov.lab8.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
