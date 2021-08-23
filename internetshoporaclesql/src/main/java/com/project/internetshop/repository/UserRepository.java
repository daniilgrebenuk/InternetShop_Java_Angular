package com.project.internetshop.repository;

import com.project.internetshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  /*Optional<User> findByUsername(String username);
  Optional<User> findByEmail(String email);*/

  Optional<User> findByUsernameOrEmail(String username, String email);
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
}
