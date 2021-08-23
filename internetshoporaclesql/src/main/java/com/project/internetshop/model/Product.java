package com.project.internetshop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(targetEntity = Category.class)
  private Category category;

  private String name;
  private int price;
  private String imageUrl;
  private boolean isAvailable;
}
