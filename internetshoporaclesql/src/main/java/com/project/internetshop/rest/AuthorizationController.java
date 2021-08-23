package com.project.internetshop.rest;

import com.project.internetshop.model.RegistrationForm;
import com.project.internetshop.model.User;
import com.project.internetshop.security.jwt.JwtTokenProvider;
import com.project.internetshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthorizationController {
  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;


  @Value("${jwt.authorization-header}")
  private String authorizationHeader;

  @Autowired
  public AuthorizationController(UserService userService, AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider,
                                 PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordEncoder = passwordEncoder;
  }


  @PostMapping("/login")
  public ResponseEntity<?> processLogin(@RequestBody RegistrationForm form,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
    try {
      authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(
              form.getUsername(),
              form.getPassword()
          ));

      User user = userService.findByUsernameOrEmail(form.getUsername());

      String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name());
      response.setHeader("Authorization", token);
      Map<String, String> res = new HashMap<>();
      res.put("token", token);

      return ResponseEntity.ok(res);

    } catch (AuthenticationException e) {
      return new ResponseEntity<>("Invalid authorization data!", HttpStatus.FORBIDDEN);
    }
  }

  @PostMapping("/registration")
  public ResponseEntity<?> processRegistration(@RequestBody @Valid RegistrationForm form, Errors errors) {
    if (errors.hasErrors() || !checkEmailIsAvailable(form.getEmail()) || !checkUsernameIsAvailable(form.getUsername())) {
      Map<String, String> map = new HashMap<>();
      errors.getFieldErrors().forEach(e -> map.put(e.getField(), e.getDefaultMessage()));
      map.put("emailIsAvailable", checkEmailIsAvailable(form.getEmail()) ? "true" : "false");
      map.put("usernameIsAvailable", checkUsernameIsAvailable(form.getUsername()) ? "true" : "false");
      return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


    User user = userService.save(form.getUser(passwordEncoder));
    log.info("Created new account with username: " + user.getUsername());

    return ResponseEntity.ok().build();
  }

  @GetMapping("/username-is-available")
  public ResponseEntity<Map<String, Boolean>> usernameIsAvailable(@RequestBody String username) {
    Map<String, Boolean> map = new HashMap<>();
    map.put("available", checkUsernameIsAvailable(username));
    return ResponseEntity.ok(map);
  }

  private boolean checkUsernameIsAvailable(String username) {
    return !userService.existsByUsername(username);
  }

  private boolean checkEmailIsAvailable(String email) {
    return !userService.existsByEmail(email);
  }


}