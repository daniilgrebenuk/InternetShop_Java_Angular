package com.project.internetshop.rest;

import com.project.internetshop.model.User;
import com.project.internetshop.repository.CategoryRepository;
import com.project.internetshop.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
public class TestController {
  private CategoryRepository categoryRepository;

  @Autowired
  public TestController(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @GetMapping("/test")
  public ResponseEntity<?> test(){
    return ResponseEntity.ok(categoryRepository.findAllActiveOrderByName());
  }

  @GetMapping("/api/v1/admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public String admin(){
    return "admin";
  }

  @GetMapping("/api/v1/user")
  @PreAuthorize("hasRole('ROLE_USER')")
  public String user(@AuthenticationPrincipal User user){
    return "user";
  }

}
