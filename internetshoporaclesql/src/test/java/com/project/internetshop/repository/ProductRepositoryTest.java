package com.project.internetshop.repository;

import com.project.internetshop.model.Category;
import com.project.internetshop.model.Order;
import com.project.internetshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Comparator;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  private final CategoryRepository categoryRepository;

  private List<Product> products;

  @Autowired
  public ProductRepositoryTest(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @BeforeEach
  void setUp() {
    products = productRepository.findAll();
  }

  @Test
  @Disabled
  void findAllByOrder_id() {

  }

  @Test
  void findAllByCategory_IdOrderByName() {
    List<Category> categories = categoryRepository.findAll();

    for (int[] i = {0}; i[0] < Math.min(categories.size(), 3); i[0]++) {
      assertEquals(
          products
              .stream()
              .filter(p -> p.getCategory().equals(categories.get(i[0])))
              .sorted(Comparator.comparing(Product::getName))
              .toList(),
          productRepository.findAllByCategory_IdOrderByName(categories.get(i[0]).getId())
      );
    }
  }

  @Test
  void findAllByOrderByNameAscAndDesc() {
    assertEquals(
        products
            .stream()
            .sorted(Comparator.comparing(Product::getName))
            .toList(),
        productRepository.findAllByOrderByNameAsc()
    );

    assertEquals(
        products
            .stream()
            .sorted(Comparator.comparing(Product::getName).reversed())
            .toList(),
        productRepository.findAllByOrderByNameDesc()
    );
  }

  @Test
  void findAllByOrderByPriceAscAndDesc() {

    assertEquals(
        products
            .stream()
            .sorted(Comparator.comparing(Product::getPrice))
            .toList(),
        productRepository.findAllByOrderByPriceAsc()
    );

    assertEquals(
        products
            .stream()
            .sorted(Comparator.comparing(Product::getPrice).reversed())
            .toList(),
        productRepository.findAllByOrderByPriceDesc()
    );

  }
}