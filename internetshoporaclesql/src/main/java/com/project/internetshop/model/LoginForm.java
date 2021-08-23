package com.project.internetshop.model;

import lombok.Data;

@Data
public class LoginForm {
  private String usernameOrEmail;
  private String password;
}
