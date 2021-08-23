package com.project.internetshop.repository;

import com.project.internetshop.model.Order;
import com.project.internetshop.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class OrderRepositoryTest {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  void findAllByUserId() {
    List<User> users = userRepository.findAll();
    List<Order> orders = orderRepository.findAll();


    for (int[] i = {0}; i[0] < Math.min(users.size(), 3); i[0]++) {
      assertEquals(
          orders
              .stream()
              .filter(o -> o.getUser().equals(users.get(i[0])))
              .toList(),
          orderRepository.
              findAllByUserId(users.get(i[0]).getId())
      );
    }
  }
}