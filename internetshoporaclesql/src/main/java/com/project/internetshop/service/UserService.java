package com.project.internetshop.service;

import com.project.internetshop.model.User;
import com.project.internetshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  public User findByUsernameOrEmail(String usernameOrEmail) {
    return userRepository
        .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist!"));
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public void delete(User user) {
    userRepository.delete(user);
  }
}
