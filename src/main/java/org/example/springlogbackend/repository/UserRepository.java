package org.example.springlogbackend.repository;

import org.example.springlogbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByDeleted(boolean deleted);
    Optional<User> findByEmailAndDeleted(String email, boolean deleted);
    Optional<User> findByIdAndDeleted(String id, boolean deleted);
}
