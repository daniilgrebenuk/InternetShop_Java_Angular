package com.project.internetshop.rest;

import com.project.internetshop.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

  @GetMapping("/current")
  public ResponseEntity<?> getCurrentUserInfo(@AuthenticationPrincipal User user){
    if (user != null){
      Map<String, String> response = new HashMap<>();
      response.put("role",user.getRole().name());
      response.put("username",user.getUsername());

      return ResponseEntity.ok(response);
    }

    return new ResponseEntity<>("Not signed in!", HttpStatus.FORBIDDEN);
  }
}
