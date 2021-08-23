package com.project.internetshop.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Users")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true)
  private String username;
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String address;

  private boolean isEnable;
  private boolean isNonLocked;

  public User(String username, String password, String email, String firstName, String lastName, String phone, String address) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.address = address;
    this.role = Role.USER;
    this.isEnable = true;
    this.isNonLocked = true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return isEnable;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isEnable;
  }

  @Override
  public boolean isEnabled() {
    return isEnable;
  }
}
