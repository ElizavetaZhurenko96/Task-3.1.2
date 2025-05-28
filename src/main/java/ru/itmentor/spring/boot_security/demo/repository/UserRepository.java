package ru.itmentor.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> getUserById(Long id);

   @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles")
   List<User> findAllWithRoles();

   @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.name = :name")
   Optional<User> findByUsernameWithRoles(@Param("name") String name);
}
