package com.project.internetshop.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Orders")
public class Order{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(targetEntity = User.class)
  private User user;

  @ManyToMany(targetEntity = Product.class)
  private List<Product> products;

  private Date createdAt;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private String address;

  @Enumerated(EnumType.STRING)
  private Status status;

  public Order(User user) {
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.phone = user.getPhone();
    this.address = user.getAddress();
  }

  public Order() {
  }
}
