package org.example.springlogbackend.repository;

import org.example.springlogbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndDeleted(String email, boolean deleted);
    Optional<User> findByIdAndDeleted(String id, boolean deleted);
}
