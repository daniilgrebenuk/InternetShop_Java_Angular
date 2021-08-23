package com.project.internetshop.repository;

import com.project.internetshop.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class CategoryRepositoryTest {

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ProductRepository productRepository;

  private List<Category> categories;

  @BeforeEach
  void setUp() {
    categories = categoryRepository.findAll();
  }

  @Test
  void findAllActive() {

    assertEquals(
        categories
            .stream()
            .filter(c->productRepository.findAllByCategory_IdOrderByName(c.getId()).size() != 0)
            .sorted(Comparator.comparing(Category::getName))
            .toList(),
        categoryRepository.findAllActiveOrderByName()
    );
  }

  @Test
  void findAllByOrderByNameDescAndAsc() {
    assertEquals(
        categories
            .stream()
            .sorted(Comparator.comparing(Category::getName))
            .toList(),
        categoryRepository.findAllByOrderByNameAsc()
    );

    assertEquals(
        categories
            .stream()
            .sorted(Comparator.comparing(Category::getName).reversed())
            .toList(),
        categoryRepository.findAllByOrderByNameDesc()
    );
  }
}